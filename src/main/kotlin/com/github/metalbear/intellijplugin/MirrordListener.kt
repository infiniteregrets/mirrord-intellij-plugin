package com.github.metalbear.intellijplugin

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import javax.swing.JCheckBox


class MirrordListener : ExecutionListener {
    private val mirrordEnv: LinkedHashMap<String, String> = LinkedHashMap()
    private val log: Logger = Logger.getInstance("MirrordListener")

    init {
        mirrordEnv["DYLD_INSERT_LIBRARIES"] = "target/debug/libmirrord_layer.dylib"
        mirrordEnv["LD_PRELOAD"] = "target/debug/libmirrord_layer.so"
        mirrordEnv["RUST_LOG"] = "DEBUG"
        mirrordEnv["MIRRORD_AGENT_IMPERSONATED_POD_NAME"] = "nginx-deployment-66b6c48dd5-ggd9n"
        mirrordEnv["MIRRORD_ACCEPT_INVALID_CERTIFICATES"] = "true"

        log.debug("Default mirrord environment variables set.")
    }

    companion object {
        var enabled: Boolean = false
        var envSet: Boolean = false
    }

    override fun processStarting(executorId: String, env: ExecutionEnvironment) {
        if (enabled) {
            val kubeDataProvider = KubeDataProvider()
            val pods = JBList<String>(kubeDataProvider.getKubeData("default"))

            val fileOpsCheckbox = JCheckBox("Enable File Operations")
            val remoteDnsCheckbox = JCheckBox("Enable Remote DNS")

            var dialogBuilder = MirrordDialogBuilder().createDialog(pods, fileOpsCheckbox, remoteDnsCheckbox)

            val response = dialogBuilder.show() == DialogWrapper.OK_EXIT_CODE
            if (response) {

                mirrordEnv["MIRRORD_AGENT_IMPERSONATED_POD_NAME"] = pods.selectedValue as String
                mirrordEnv["MIRRORD_FILE_OPS"] = fileOpsCheckbox.isSelected.toString()
                mirrordEnv["MIRRORD_REMOTE_DNS"] = remoteDnsCheckbox.isSelected.toString()

                var envMap = getPythonEnv(env)
                envMap.putAll(mirrordEnv)

                log.debug("mirrord env set")
                envSet = true
            }
        }
        super.processStarting(executorId, env)
    }

    override fun processTerminating(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (enabled and envSet) {
            var envMap = getPythonEnv(env)
            for (key in mirrordEnv.keys) {
                if (mirrordEnv.containsKey(key)) {
                    envMap.remove(key)
                }
            }
            log.debug("mirrord env reset")
        }
        super.processTerminating(executorId, env, handler)
    }

    private fun getPythonEnv(env: ExecutionEnvironment): LinkedHashMap<String, String> {
        log.debug("fetching python env")
        var envMethod = env.runProfile.javaClass.getMethod("getEnvs")
        return envMethod.invoke(env.runProfile) as LinkedHashMap<String, String>
    }
}
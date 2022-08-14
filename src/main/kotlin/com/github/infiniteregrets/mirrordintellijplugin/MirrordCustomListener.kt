package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel


class MirrordCustomListener : ExecutionListener {
    private val mirrordEnv: LinkedHashMap<String, String> = LinkedHashMap()
    private val namespace: String = "default"

    init {
        mirrordEnv["DYLD_INSERT_LIBRARIES"] = "target/debug/libmirrord_layer.dylib"
        mirrordEnv["RUST_LOG"] = "DEBUG"
        mirrordEnv["MIRRORD_AGENT_IMPERSONATED_POD_NAME"] = "nginx-deployment-66b6c48dd5-ggd9n"
        mirrordEnv["MIRRORD_ACCEPT_INVALID_CERTIFICATES"] = "true"
    }
    companion object {
        var enabled: Boolean = false
    }
    override fun processStarting(executorId: String, env: ExecutionEnvironment) {
        if (enabled) {

            var dialogBuilder = DialogBuilder()

            val dialogPanel = JPanel(BorderLayout())
            val label = JLabel("Select pod to impersonate")
            label.preferredSize = Dimension(100, 100)
            dialogPanel.add(label, BorderLayout.NORTH)
            var pods = getKubeData("default")
            val jlistPods = JList(pods.toArray())
            dialogPanel.add(jlistPods, BorderLayout.CENTER)

            dialogBuilder.setCenterPanel(dialogPanel)
            dialogBuilder.setTitle("Mirrord")

            val response = dialogBuilder.show() === DialogWrapper.OK_EXIT_CODE
            if (response) {
                val selectedPod = jlistPods.selectedValue as String
                mirrordEnv["MIRRORD_AGENT_IMPERSONATED_POD_NAME"] = selectedPod
                var envMap = getPythonEnv(env)
                envMap.putAll(mirrordEnv)
            }
        }
        super.processStarting(executorId, env)
    }

    override fun processTerminating(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (enabled) {
            var envMap = getPythonEnv(env)
            for (key in mirrordEnv.keys) {
                envMap.remove(key)
            }
        }
        super.processTerminating(executorId, env, handler)
    }

    private fun getPythonEnv(env: ExecutionEnvironment): LinkedHashMap<String, String> {
        var envMethod = env.runProfile.javaClass.getMethod("getEnvs")
        return envMethod.invoke(env.runProfile) as LinkedHashMap<String, String>
    }

}

private  fun getKubeData(namespace: String): ArrayList<String> {
    var client: ApiClient = Config.defaultClient()
    Configuration.setDefaultApiClient(client)

    val api = CoreV1Api()
    val pods = api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null,null)

    var list = ArrayList<String>()
    for (pod in pods.items) {
        list.add(pod.metadata!!.name!!);
    }
    return list
}
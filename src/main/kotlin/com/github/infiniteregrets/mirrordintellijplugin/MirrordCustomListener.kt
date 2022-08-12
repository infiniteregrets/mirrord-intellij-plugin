package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config
import java.awt.Point

class MirrordCustomListener : ExecutionListener {
    private val mirrordEnv: LinkedHashMap<String, String> = LinkedHashMap()

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
            var envMap = getPythonEnv(env)
            val pods = getKubeData("default")

//            JBPopupFactory
//                    .getInstance()
//                    .createListPopup(PopUpMenu("Select a pod to impersonate", pods))
//                    .show(RelativePoint.fromScreen(Point(600, 200)))
            val response = CustomDialog().showAndGet()
            envMap.putAll(mirrordEnv)
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

}
package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import kotlin.collections.LinkedHashMap

class MirrordCustomListener : ExecutionListener {
    val mirrordEnv: LinkedHashMap<String, String> = LinkedHashMap()

    init {
        mirrordEnv.put("LD_PRELOAD", "target/debug/libmirrord.so")
        mirrordEnv.put("RUST_LOG", "DEBUG")
        mirrordEnv.put("MIRRORD_AGENT_IMPERSONATED_POD_NAME", "nginx-deployment-66b6c48dd5-ggd9n")
        mirrordEnv.put("MIRRORD_ACCEPT_INVALID_CERTIFICATES", "true")
    }
    companion object {
        var enabled: Boolean = false
    }
    override fun processStarting(executorId: String, env: ExecutionEnvironment) {
        if (enabled) {
            var envMap = getPythonEnv(env)
            envMap.putAll(mirrordEnv)
        }
        super.processStarting(executorId, env)
    }

    override fun processTerminating(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (enabled) {
            for (key in mirrordEnv.keys) {
                mirrordEnv.remove(key)
            }
        }
        super.processTerminating(executorId, env, handler)
    }

    private fun getPythonEnv(env: ExecutionEnvironment): LinkedHashMap<String, String> {
        var envMethod = env.runProfile.javaClass.getMethod("getEnvs")
        return envMethod.invoke(env.runProfile) as LinkedHashMap<String, String>
    }

}
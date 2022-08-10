package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import kotlin.collections.LinkedHashMap

class MirrordCustomListener : ExecutionListener {

    override fun processStarting(executorId: String, env: ExecutionEnvironment) {
        var methods = env.runProfile.javaClass.methods
        var envMethod = env.runProfile.javaClass.getMethod("getEnvs")
        var map: LinkedHashMap<String, String> = envMethod.invoke(env.runProfile) as LinkedHashMap<String, String>
        map.put("MIRRORD_LD_PRELOAD", "target/debug/libmirrord.so")

        super.processStarting(executorId, env)
    }

    override fun processTerminating(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        var envMethod = env.runProfile.javaClass.getMethod("getEnvs")
        var map: LinkedHashMap<String, String> = envMethod.invoke(env.runProfile) as LinkedHashMap<String, String>
        map.remove("MIRRORD_LD_PRELOAD")
        super.processTerminating(executorId, env, handler)
    }

}
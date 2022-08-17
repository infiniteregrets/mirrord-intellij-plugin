package com.github.metalbear.intellijplugin

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config

class KubeDataProvider {
    fun getKubeData(namespace: String): ArrayList<String> {
        var client: ApiClient = Config.defaultClient()
        Configuration.setDefaultApiClient(client)

        val api = CoreV1Api()
        val pods = api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null, null)

        var list = ArrayList<String>()
        for (pod in pods.items) {
            list.add(pod.metadata!!.name!!);
        }
        return list
    }
}
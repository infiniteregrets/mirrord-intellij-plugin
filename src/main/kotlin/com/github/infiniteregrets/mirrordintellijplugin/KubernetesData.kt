package com.github.infiniteregrets.mirrordintellijplugin

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config


class KubernetesData {
    public fun getNameSpacedPods(namespace: String) {
        val client: ApiClient = Config.defaultClient()
        Configuration.setDefaultApiClient(client)

        val api = CoreV1Api()
        val pods = api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null,null)

        var list = ArrayList<String>()
        for (pod in pods.items) {
            list.add(pod.metadata!!.name!!);
        }
    }
}
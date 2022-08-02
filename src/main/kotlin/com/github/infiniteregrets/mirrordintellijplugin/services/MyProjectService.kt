package com.github.infiniteregrets.mirrordintellijplugin.services

import com.intellij.openapi.project.Project
import com.github.infiniteregrets.mirrordintellijplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

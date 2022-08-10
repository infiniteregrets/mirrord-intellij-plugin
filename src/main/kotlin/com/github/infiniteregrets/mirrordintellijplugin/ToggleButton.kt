package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class ToggleButton : ToggleAction() {

    override fun isSelected(e: AnActionEvent): Boolean {
        return MirrordCustomListener.enabled
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        MirrordCustomListener.enabled = state
    }
}
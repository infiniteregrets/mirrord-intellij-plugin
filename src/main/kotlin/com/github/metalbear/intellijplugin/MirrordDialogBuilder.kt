package com.github.metalbear.intellijplugin

import com.intellij.openapi.ui.DialogBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

class MirrordDialogBuilder {

    fun createDialog(jlistPods: JList<Any>): DialogBuilder {
        var dialogBuilder = DialogBuilder()
        val dialogPanel = JPanel(BorderLayout())

        val label = JLabel("Select pod to impersonate")

        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.NORTH)

        dialogPanel.add(jlistPods, BorderLayout.CENTER)

        dialogBuilder.setCenterPanel(dialogPanel)
        dialogBuilder.setTitle("mirrord")

        return dialogBuilder
    }
}
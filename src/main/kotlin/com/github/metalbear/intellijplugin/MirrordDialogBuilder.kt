package com.github.metalbear.intellijplugin

import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBList
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel

class MirrordDialogBuilder {
    private val heading: String = "mirrord"
    private val labelName: String = "Select pod to impersonate"
    fun createDialog(pods: JBList<String>, fileOpsCheckbox: JCheckBox, remoteDnsCheckbox: JCheckBox): DialogBuilder {
        var dialogBuilder = DialogBuilder()
        val dialogPanel = JPanel(BorderLayout())

        val label = JLabel(labelName)

        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.NORTH)

        dialogPanel.add(pods, BorderLayout.CENTER)

        dialogPanel.add(fileOpsCheckbox, BorderLayout.SOUTH)
        dialogPanel.add(remoteDnsCheckbox, BorderLayout.SOUTH)

        dialogBuilder.setCenterPanel(dialogPanel)
        dialogBuilder.setTitle(heading)

        return dialogBuilder
    }
}
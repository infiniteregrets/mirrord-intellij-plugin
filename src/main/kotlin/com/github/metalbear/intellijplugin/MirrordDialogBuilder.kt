package com.github.metalbear.intellijplugin

import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBList
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.border.EmptyBorder


class MirrordDialogBuilder {
    private val heading: String = "mirrord"
    private val labelName: String = "Select pod to impersonate"
    private val optionLabel: String = "Options"
    fun createDialog(pods: JBList<String>, fileOpsCheckbox: JCheckBox, remoteDnsCheckbox: JCheckBox): DialogBuilder {
        var dialogBuilder = DialogBuilder()
        val dialogPanel = JPanel(BorderLayout())

        val label = JLabel(labelName)

        val podPanel = JPanel(GridLayout(2, 1, 10, 5))
        podPanel.add(label, BorderLayout.NORTH)
        podPanel.add(pods)

        dialogPanel.add(podPanel, BorderLayout.WEST)

        dialogPanel.add(JSeparator(JSeparator.VERTICAL),
                 BorderLayout.CENTER);

        val checkBoxPanel = JPanel(GridLayout(2, 1, 10, 2))
        checkBoxPanel.add(fileOpsCheckbox)
        checkBoxPanel.add(remoteDnsCheckbox)

        dialogPanel.add(checkBoxPanel, BorderLayout.EAST)

        dialogBuilder.setCenterPanel(dialogPanel)
        dialogBuilder.setTitle(heading)

        return dialogBuilder
    }
}
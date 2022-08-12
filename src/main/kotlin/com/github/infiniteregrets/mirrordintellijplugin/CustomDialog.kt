package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel


class CustomDialog : DialogWrapper(true){
    init {
        init()
        title = "Sample Dialog"
    }
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        val label = JLabel("Select pod to impersonate")
        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.NORTH)
        val data = arrayOf("one", "two", "three", "four")
        dialogPanel.add(JList(data), BorderLayout.CENTER)
        return dialogPanel
    }
}
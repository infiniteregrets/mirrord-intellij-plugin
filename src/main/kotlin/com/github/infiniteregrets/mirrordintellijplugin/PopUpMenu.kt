package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep

class PopUpMenu(title: String, values: ArrayList<String>) : BaseListPopupStep<String>() {
    init {
        init(title, values.toMutableList(), null)
    }

    override fun onChosen(selectedValue: String?, finalChoice: Boolean): PopupStep<*>? {

        if (selectedValue != null) {
            updateMirrordEnv(selectedValue)
        }
        return super.onChosen(selectedValue, finalChoice)
    }

    private fun updateMirrordEnv(value: String) {

    }
}
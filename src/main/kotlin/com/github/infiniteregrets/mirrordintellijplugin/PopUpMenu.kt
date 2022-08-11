package com.github.infiniteregrets.mirrordintellijplugin

import com.intellij.openapi.ui.popup.*
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import javax.swing.Icon

class PopUpMenu(display: String, values: ArrayList<String>) : ListPopupStep<String> {
    override fun getTitle(): String? {
        return "Select Pod to impersonate"
    }

    override fun canceled() {
    }

    override fun isMnemonicsNavigationEnabled(): Boolean {
        return false
    }

    override fun getMnemonicNavigationFilter(): MnemonicNavigationFilter<String>? {
        return null
    }

    override fun isSpeedSearchEnabled(): Boolean {
        return false
    }

    override fun getSpeedSearchFilter(): SpeedSearchFilter<String>? {
        return null
    }

    override fun isAutoSelectionEnabled(): Boolean {
        return false
    }

    override fun getFinalRunnable(): Runnable? {
        return null
    }

    override fun getValues(): ArrayList<String> {
        return values
    }

    override fun getDefaultOptionIndex(): Int {
        return 0
    }

    override fun getSeparatorAbove(value: String?): ListSeparator? {
        return null
    }

    override fun getTextFor(value: String?): String {
        return ""
    }

    override fun getIconFor(value: String?): Icon? {
        return null
    }

    override fun isSelectable(value: String?): Boolean {
        return true
    }

    override fun hasSubstep(selectedValue: String?): Boolean {
        return false
    }

    override fun onChosen(selectedValue: String?, finalChoice: Boolean): PopupStep<*>? {
        return null
    }

}
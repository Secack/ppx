@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.data.XPrefs
import com.akari.ppx.data.model.CheckBoxItem
import com.akari.ppx.utils.*
import com.akari.ppx.xp.hook.BaseHook

class LayoutHook : BaseHook {
    override fun onHook() {
        val pref = XPrefs<String>("enable_double_layout_style")
        pref.check("") { return }
        val items = pref.fromJsonList<CheckBoxItem>()
        setSettingKeyValue { key ->
            items.find {
                it.key == key
            }?.checked
        }
    }
}
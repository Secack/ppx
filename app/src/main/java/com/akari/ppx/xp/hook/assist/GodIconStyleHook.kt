@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.check
import com.akari.ppx.utils.setSettingKeyValue
import com.akari.ppx.xp.hook.SwitchHook

class GodIconStyleHook : SwitchHook("enable_old_god_icon_style") {
    override fun onHook() {
        setSettingKeyValue {
            it.check("common_god_icon_style") { 0 }
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.app.Activity
import com.akari.ppx.utils.callMethod
import com.akari.ppx.utils.check
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.utils.setSettingKeyValue
import com.akari.ppx.xp.Init.photoEditorCallbackClass
import com.akari.ppx.xp.Init.photoEditorLauncher
import com.akari.ppx.xp.Init.photoEditorLauncherClass
import com.akari.ppx.xp.Init.photoEditorParamsClass
import com.akari.ppx.xp.hook.SwitchHook

class HighlightHook : SwitchHook("unlock_highlight") {
    override fun onHook() {
        setSettingKeyValue {
            it.check("bds_enable_highlight") { true }
        }
        photoEditorLauncherClass!!.replaceMethod(
            photoEditorLauncher(),
            Activity::class.java,
            String::class.java,
            photoEditorCallbackClass!!.name,
            photoEditorParamsClass!!.name
        ) { param ->
            param.args[2].callMethod("a", param.args[1], false)
        }
    }
}
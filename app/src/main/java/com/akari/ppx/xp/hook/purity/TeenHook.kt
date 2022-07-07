@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.app.Activity
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class TeenHook : SwitchHook("remove_teenager_dialog") {
    override fun onHook() {
        "com.sup.superb.m_teenager.TeenagerService".replaceMethod(
            cl,
            "tryShowTeenagerModeDialog",
            Activity::class.java
        ) {}
    }
}
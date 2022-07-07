@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class V1080PHook : SwitchHook("unlock_1080p_limit") {
    override fun onHook() {
        "com.sup.android.m_chooser.impl.c".replaceMethod(
            cl,
            "b",
            Int::class.java,
            Int::class.java
        ) { true }
    }
}
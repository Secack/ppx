@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.content.Context
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class UpdateHook : SwitchHook("disable_update") {
    override fun onHook() {
        "com.sup.android.m_update.UpdateService".replaceMethod(
            cl,
            "checkUpdateByAutomatic",
            Context::class.java
        ) {}
    }
}
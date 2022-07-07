@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class WordHook : SwitchHook("unlock_illegal_words") {
    override fun onHook() {
        "com.sup.android.module.publish.view.NewInputCommentDialog\$tryPublish$2".replaceMethod(
            cl,
            "invoke",
            String::class.java
        ) { param ->
            param.args[0]
        }
        "com.sup.android.m_illegalword.utils.RuleTable".replaceMethod(cl, "getReplaceMap") { null }
    }
}
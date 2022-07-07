@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.content.Context
import com.akari.ppx.utils.hookBeforeConstructor
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.absFeedCellClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.shareViewClass
import com.akari.ppx.xp.hook.SwitchHook

class ShareHook : SwitchHook("simplify_share") {
    override fun onHook() {
        shareViewClass!!.hookBeforeConstructor(
            Context::class.java,
            "[Lcom.sup.android.i_sharecontroller.model.ShareInfo;",
            "[Lcom.sup.android.i_sharecontroller.model.OptionAction\$OptionActionType;",
            "com.sup.android.i_sharecontroller.model.OptionAction\$OptionActionListener",
            absFeedCellClass
        ) { param ->
            param.args[1] = null
        }
        "com.sup.android.m_sharecontroller.service.BaseShareService".replaceMethod(
            cl,
            "getAllShareletTypes",
            Context::class.java,
            Int::class.java
        ) { null }
    }
}
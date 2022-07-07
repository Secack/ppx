@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.callMethodAs
import com.akari.ppx.utils.check
import com.akari.ppx.utils.findClass
import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class InteractionStyleHook : SwitchHook("modify_interaction_style") {
    override fun onHook() {
        val (diggStyle, dissStyle) = listOf<String>(
            XPrefs("digg_style"),
            XPrefs("diss_style")
        ).map { if (it.isEmpty()) 10 else it.toInt() }
        "com.sup.android.mi.feed.repo.bean.cell.AbsFeedItem\$ItemRelation".findClass(cl).apply {
            hookAfterMethod("getDiggType") { param ->
                param.thisObject.callMethodAs<Boolean>("isLike").check(false) {
                    param.result = diggStyle
                }
            }
            hookAfterMethod("getDissType") { param ->
                param.thisObject.callMethodAs<Boolean>("isDiss").check(false) {
                    param.result = dissStyle
                }
            }
        }
    }
}
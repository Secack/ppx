@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.utils.check
import com.akari.ppx.utils.getIntField
import com.akari.ppx.utils.getObjectFieldAs
import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class AvatarHook : SwitchHook("remove_avatar_decoration") {
    override fun onHook() {
        "com.sup.android.mi.usercenter.model.UserInfo".hookAfterMethod(
            cl,
            "getDecorationList"
        ) { param ->
            val list = param.result as ArrayList<*>? ?: return@hookAfterMethod
            list.indices.reversed().forEach { i ->
                list[i].getObjectFieldAs<ArrayList<*>>("decorationInfos")[0]
                    .getIntField("decorationType").check(2) {
                        list.removeAt(i)
                    }
            }
        }
    }
}
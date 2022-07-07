@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.utils.findClass
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.mainActivityClass
import com.akari.ppx.xp.hook.SwitchHook

class RedDotHook : SwitchHook("remove_red_dots") {
    override fun onHook() {
        "com.sup.superb.feedui.view.tabv2.FeedTabFragmentV2".findClass(cl).apply {
            hookBeforeMethod(declaredMethods.find { m ->
                m.parameterTypes.size == 3 && m.parameterTypes[0] == Boolean::class.java
                        && m.parameterTypes[1] == Int::class.java && m.parameterTypes[2] == Int::class.java
            }?.name, Boolean::class.java, Int::class.java, Int::class.java) { param ->
                param.args[0] = false
                param.args[1] = 0
            }
        }
        mainActivityClass?.hookBeforeMethod("handleBadgeAndPopup", Long::class.java) { param ->
            param.args[0] = 0L
        }
        "com.sup.android.m_mine.view.subview.MyProfileHeaderLayout".hookBeforeMethod(
            cl,
            "a",
            Boolean::class.java
        ) { param ->
            param.args[0] = false
        }

    }
}
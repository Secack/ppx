@file:Suppress("unused")

package com.akari.ppx.xp.hook.misc

import com.akari.ppx.utils.callMethod
import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.mainActivityClass
import com.akari.ppx.xp.hook.SwitchHook

class BottomViewHook : SwitchHook("remove_bottom_view") {
    override fun onHook() {
        mainActivityClass?.apply {
            hookBeforeMethod("channelFragmentIsSelected", Boolean::class.java) { param ->
                param.args[0] = true
            }
            hookAfterMethod("onWindowFocusChanged", Boolean::class.java) { param ->
                param.thisObject.callMethod("channelFragmentIsSelected", false)
            }
        }
    }
}
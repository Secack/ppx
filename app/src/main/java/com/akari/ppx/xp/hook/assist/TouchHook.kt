@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.view.MotionEvent
import com.akari.ppx.utils.callMethod
import com.akari.ppx.utils.getObjectField
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.vControllerHandlerClass
import com.akari.ppx.xp.hook.SwitchHook

class TouchHook : SwitchHook("prevent_mistouch") {
    override fun onHook() {
        vControllerHandlerClass!!.apply {
            hookBeforeMethod("dispatchTouchEvent", MotionEvent::class.java) { param ->
                runCatching {
                    with(param.thisObject) {
                        callMethod("getActivity")?.getObjectField("mAccountService")
                        callMethod("setGestureEnable", false)
                    }
                }
            }
            hookBeforeMethod("setGestureEnable", Boolean::class.java) { param ->
                param.args[0] = false
            }
        }
    }
}
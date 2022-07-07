@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.view.MotionEvent
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.vMotionEventHandler
import com.akari.ppx.xp.Init.vMotionEventHandlerClass
import com.akari.ppx.xp.hook.SwitchHook
import java.util.*

class KeepSpeedHook : SwitchHook("keep_video_play_speed") {
    override fun onHook() {
        val (isFirstPress, isLongPressing) = listOf(BooleanArray(2), BooleanArray(2))
        "com.sup.android.supvideoview.videoview.SupVideoView".hookAfterMethod(
            cl,
            "getPlayState"
        ) { param ->
            val state = param.result as Int
            if (state == 0 || state == 5)
                Arrays.fill(isFirstPress, false)
        }
        vMotionEventHandlerClass!!.apply {
            hookBeforeMethod("onLongPress", MotionEvent::class.java) {
                isLongPressing[0] = true
                isFirstPress[0] = !isFirstPress[0]
            }
            replaceMethod(vMotionEventHandler(), MotionEvent::class.java) { param ->
                isLongPressing[0].check(true) {
                    isFirstPress[0].check(false) { param.invokeOriginalMethod() }
                    isLongPressing[0] = false
                }
            }
        }
        "com.sup.superb.video.controllerlayer.j".findClass(cl).apply {
            val scope = { block: () -> Unit ->
                Thread.currentThread().stackTrace.find { it.className.startsWith("com.sup.superb.video.viewholder") }
                    ?.run { block() }
            }
            hookBeforeMethod("z") {
                scope {
                    isLongPressing[1] = true
                    isFirstPress[1] = !isFirstPress[1]
                }
            }
            replaceMethod("A") { param ->
                scope {
                    isLongPressing[1].check(true) {
                        isFirstPress[1].check(false) { param.invokeOriginalMethod() }
                        isLongPressing[1] = false
                    }
                }
                true
            }
        }
    }
}
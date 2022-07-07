@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.check
import com.akari.ppx.utils.findClass
import com.akari.ppx.utils.getStaticObjectFieldAs
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.BaseHook
import java.math.BigDecimal

class PlaySpeedHook : BaseHook {
    override fun onHook() {
        val (normalPlaySpeed, pressedPlaySpeed) = listOf<String>(
            XPrefs("normal_play_speed"),
            XPrefs("pressed_play_speed")
        ).mapIndexed { i, v -> if (v.isEmpty()) 1.0f * (i + 1) else v.toFloat() }
        "com.sup.android.ttvideoplayer.TTVideoEnginePlayer".hookBeforeMethod(
            cl,
            "setPlaySpeed",
            Float::class.java
        ) { param ->
            when (param.args[0] as Float) {
                1.0f -> param.args[0] = normalPlaySpeed
                2.0f -> param.args[0] = pressedPlaySpeed
            }
        }
        val targetId = "com.sup.superb.video.R\$id".findClass(cl)
            .getStaticObjectFieldAs<Int>("video_speed_tip_tv")
        TextView::class.java.name.hookBeforeMethod(
            cl,
            "setText",
            CharSequence::class.java
        ) { param ->
            with(param.thisObject as TextView) {
                id.check(targetId) {
                    val speed =
                        BigDecimal(pressedPlaySpeed.toString()).stripTrailingZeros().toPlainString()
                    param.args[0] = SpannableString("${speed}X快进中").apply {
                        setSpan(ForegroundColorSpan(-38784), 0, speed.length + 1, 33)
                    }
                }
            }
        }
    }
}
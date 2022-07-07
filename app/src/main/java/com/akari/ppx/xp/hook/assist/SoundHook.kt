@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.media.SoundPool
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class SoundHook : SwitchHook("enable_digg_sound") {
    override fun onHook() {
        "com.sup.android.manager.ClickSoundManager".findClass(cl).apply {
            replaceMethod(
                "playSound",
                String::class.java,
                Int::class.java
            ) { param ->
                val soundPool = getStaticObjectFieldOrNullAs<SoundPool>("soundPool") ?: run {
                    callMethod("initSoundPool")
                    getStaticObjectFieldAs("soundPool")
                }
                getStaticObjectFieldAs<HashMap<String, Int>>("cacheMap")[param.args[0]]?.let {
                    soundPool.play(it, 1f, 1f, 1, param.args[1] as Int, 1f)
                }
            }
        }
    }
}
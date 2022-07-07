@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.utils.setIntField
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class EmojiHook : SwitchHook("unlock_emoji_limit") {
    override fun onHook() {
        "com.sup.android.emoji.EmojiService".hookBeforeMethod(
            cl,
            "collectEmoticon",
            "com.sup.android.base.model.ImageModel",
            Long::class.java,
            Long::class.java,
            Long::class.java,
            "com.sup.android.superb.i_emoji.IEmojiService\$SingleCallBack"
        ) { param ->
            param.thisObject.setIntField("EMOTICON_MAX_COUNT", Int.MAX_VALUE)
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.widget.ImageView
import com.akari.ppx.utils.check
import com.akari.ppx.utils.findClass
import com.akari.ppx.utils.getStaticObjectFieldAs
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class VCommentHook : SwitchHook("unlock_video_comment_limit") {
    override fun onHook() {
        val targetId = "com.sup.android.module.publish.R\$id".findClass(cl)
            .getStaticObjectFieldAs<Int>("iv_comment_video")
        ImageView::class.java.name.hookBeforeMethod(cl, "setVisibility", Int::class.java) { param ->
            with(param.thisObject as ImageView) {
                id.check(targetId) {
                    param.args[0] = 0
                }
            }
        }
    }
}
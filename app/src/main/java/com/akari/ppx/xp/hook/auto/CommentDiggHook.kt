@file:Suppress("unused")

package com.akari.ppx.xp.hook.auto

import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.commentHolder
import com.akari.ppx.xp.Init.commentHolderClass
import com.akari.ppx.xp.hook.SwitchHook

class CommentDiggHook : SwitchHook("auto_comment_digg") {
    override fun onHook() {
        "com.sup.android.mi.publish.bean.CommentBean".hookBeforeMethod(
            cl,
            "setRealCommentId",
            Long::class.java
        ) { param ->
            diggCell(cellId = param.args[0] as Long)
        }
        commentHolderClass!!.hookAfterMethod(commentHolder(), commentHolderClass) { param ->
            runCatching {
                param.result.callMethod("getComment")?.apply {
                    if (callMethodAs<Long>("getCommentId") < 0L) {
                        callMethod("setHasLiked", true)
                        callMethod("setLikeCount", 1L)
                    }
                }
            }
        }
    }
}
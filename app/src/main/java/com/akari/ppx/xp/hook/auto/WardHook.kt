@file:Suppress("unused")

package com.akari.ppx.xp.hook.auto

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cellDigger
import com.akari.ppx.xp.Init.cellDiggerClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.godCommentDiggerClass
import com.akari.ppx.xp.hook.SwitchHook

class WardHook : SwitchHook("auto_ward") {
    override fun onHook() {
        val condition = XPrefs<String>("auto_ward_condition").let {
            if (it.isEmpty()) 0 else it.toInt()
        }
        when (condition) {
            /* 为帖子点赞时 */ 0 -> {
            cellDiggerClass!!.hookBeforeMethod(
                cellDigger(),
                Int::class.java,
                Long::class.java,
                Boolean::class.java,
                Int::class.java,
                Int::class.java
            ) { param ->
                param.args[2].check(true) {
                    wardCell(param.args[1] as Long)
                }
            }
        }
            /* 为帖子评论时 */ 1 -> {
            "com.sup.android.module.publish.publish.PublishLooper\$enqueue$1".hookBeforeMethod(
                cl,
                "invoke",
                "com.sup.android.mi.publish.bean.PublishBean"
            ) { param ->
                with(param.args[0]) {
                    if (getObjectFieldAs<Long>("fakeId") != 666L) {
                        setObjectField("fakeId", 666L)
                        wardCell(getLongField("cellId"))
                    }
                }
            }
        }
            /* 为评论送神时 */ 2 -> {
            godCommentDiggerClass!!.hookBeforeMethod(
                "a",
                Long::class.java,
                Long::class.java,
                "com.sup.android.m_comment.util.helper.g"
            ) { param ->
                wardCell(param.args[0] as Long)
            }
        }
        }
    }
}
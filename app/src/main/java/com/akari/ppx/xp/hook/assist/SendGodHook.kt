@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class SendGodHook : SwitchHook("unlock_send_god_limit") {
    override fun onHook() {
        val isAuto = XPrefs<Boolean>("auto_send_god")
        val autoTimeLimit = XPrefs<String>("auto_send_god_time_limit").let {
            if (it.isEmpty()) 86400 else it.toInt()
        }

        fun Any.shouldSendGod() =
            isAuto && System.currentTimeMillis() / 1000 - getLongField("createTime") <= autoTimeLimit

        "com.sup.android.mi.feed.repo.bean.comment.Comment".hookAfterMethod(
            cl,
            "getSendGodStatus"
        ) { param ->
            with(param.thisObject) {
                checkIf({ getLongField("aliasItemId") == 1L && !getBooleanField("hasLiked") }) {
                    setLongField("aliasItemId", 2)
                    diggCell(cellId = getLongField("commentId"))
                    if (shouldSendGod())
                        diggGodComment(getLongField("itemId"), getLongField("commentId"))
                    param.result = 1
                    return@hookAfterMethod
                }
                checkIf({ getIntField("sendGodStatus") == 3 }) {
                    if (getLongField("aliasItemId") == 2L) {
                        param.result = if (shouldSendGod()) 2 else 1
                        return@hookAfterMethod
                    }
                    setLongField("aliasItemId", 1)
                    param.result = 1
                }
            }

        }
    }
}
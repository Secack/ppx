@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.getDiffDays
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.utils.splitByOr
import com.akari.ppx.utils.ts2Date
import com.akari.ppx.xp.Init.inexactDate
import com.akari.ppx.xp.Init.inexactDateClass
import com.akari.ppx.xp.hook.SwitchHook

class CommentTimeHook : SwitchHook("show_exact_comment_time") {
    override fun onHook() {
        val recentFormat = XPrefs<String>("recent_time_format").splitByOr()
        val exactFormat = XPrefs<String>("exact_time_format")
        inexactDateClass!!.replaceMethod(
            inexactDate(),
            Long::class.java,
            "kotlin.jvm.functions.Function0"
        ) { param ->
            val ts = param.args[0] as Long
            runCatching {
                ts.ts2Date(ts.getDiffDays().let { recentFormat[it] })
            }.getOrElse {
                ts.ts2Date(exactFormat)
            }
        }
    }
}



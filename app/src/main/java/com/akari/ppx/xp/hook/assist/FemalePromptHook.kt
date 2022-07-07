@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.absFeedCellClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class FemalePromptHook : SwitchHook("enable_female_prompt") {
    override fun onHook() {
        var isFemale = false
        "com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil\$Companion".hookAfterMethod(
            cl,
            "getUserName",
            absFeedCellClass
        ) { param ->
            isFemale = 2 == param.thisObject.callMethod("getAuthorInfo", param.args[0])
                ?.callMethodAs<Int>("getGender")
        }
        val targetIds = "com.sup.android.detail.R\$id".findClass(cl).run {
            listOf<Int>(
                getStaticObjectFieldAs("detail_item_user_name_tv"),
                getStaticObjectFieldAs("detail_comment_user_name_tv")
            )
        }
        TextView::class.java.name.hookBeforeMethod(
            cl,
            "setText",
            CharSequence::class.java
        ) { param ->
            with(param.thisObject as TextView) {
                targetIds.find { it == id }?.run {
                    isFemale.check(true) {
                        val text = param.args[0] as String
                        param.args[0] = SpannableString(text).apply {
                            setSpan(ForegroundColorSpan(-38784), 0, text.length, 33)
                        }
                    }
                }
            }
        }
    }
}
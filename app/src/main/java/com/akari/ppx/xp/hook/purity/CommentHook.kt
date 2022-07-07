@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.commentResponseClass
import com.akari.ppx.xp.hook.SwitchHook
import java.util.regex.Pattern

class CommentHook : SwitchHook("remove_comments") {
    override fun onHook() {
        val (keywords, users) = listOf<String>(
            XPrefs("remove_comments_keywords"),
            XPrefs("remove_comments_users")
        ).map { it.splitByOr() }
        commentResponseClass!!.hookBeforeMethod("a", ArrayList::class.java) { param ->
            runCatching {
                val comments = param.args[0] as ArrayList<*>? ?: return@hookBeforeMethod
                comments.indices.reversed().forEach { i ->
                    run e@{
                        val comment = runCatching {
                            comments[i].callMethod("getReply")
                        }.getOrElse { comments[i].callMethod("getComment") }!!

                        fun checkPattern(list: ArrayList<String>, input: String?): Boolean {
                            input?.let {
                                list.forEach { s ->
                                    s.checkIf({ Pattern.matches(this, it) }) {
                                        comments.removeAt(i)
                                        return true
                                    }
                                }
                            }
                            return false
                        }
                        checkPattern(keywords, comment.getObjectFieldAs("text")).check(true) {
                            return@e
                        }
                        checkPattern(
                            users,
                            comment.getObjectField("userInfo")?.getObjectFieldAs("name")!!
                        ).check(true) {
                            return@e
                        }
                    }
                }
            }
        }
    }
}
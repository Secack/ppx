@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.feedResponse
import com.akari.ppx.xp.Init.feedResponseClass
import com.akari.ppx.xp.hook.BaseHook
import java.util.regex.Pattern

class FeedHook : BaseHook {
    override fun onHook() {
        val conditions = listOf<Boolean>(
            XPrefs("remove_feeds"),
            XPrefs("remove_official_feeds"),
            XPrefs("remove_promotional_feeds"),
            XPrefs("remove_live_feeds")
        )
        conditions.find { it }?.run {
            val (keywords, users) = listOf<String>(
                XPrefs("remove_feeds_keywords"),
                XPrefs("remove_feeds_users")
            ).map { it.splitByOr() }
            feedResponseClass!!.hookBeforeMethod(
                feedResponse(),
                String::class.java,
                "com.sup.android.mi.feed.repo.bean.FeedResponse",
                Boolean::class.java,
                Int::class.java
            ) { param ->
                val feeds = param.args[1].callMethodAs<ArrayList<*>>("getData")
                feeds.indices.reversed().forEach { i ->
                    run e@{
                        val removeCurrent = { feeds.removeAt(i) }
                        val item = feeds[i].callMethodOrNull("getFeedItem")
                        val user = item?.callMethodOrNull("getAuthor")
                        conditions.forEachIndexed { index, condition ->
                            condition.check(false) { return@forEachIndexed }
                            when (index) {
                                /* 屏蔽帖子 */ 0 -> {
                                fun checkPattern(list: ArrayList<String>, input: String?): Boolean {
                                    input?.let { s ->
                                        list.find {
                                            Pattern.matches(it, s)
                                        }?.run {
                                            removeCurrent()
                                            return true
                                        }
                                    }
                                    return false
                                }
                                checkPattern(
                                    keywords,
                                    item?.callMethodOrNullAs("getContent")
                                ).check(true) {
                                    return@e
                                }
                                checkPattern(users, user?.callMethodOrNullAs("getName"))
                                    .check(true) {
                                        return@e
                                    }
                            }
                                /* 屏蔽官方账号帖子 */ 1 -> {
                                user?.callMethodOrNull("getCertifyInfo")
                                    ?.callMethodOrNullAs<String>("getDescription")?.checkIf({
                                    contains("官方账号") || contains("视频号") || contains("新媒体")
                                }) {
                                    removeCurrent()
                                    return@e
                                }
                            }
                                /* 屏蔽带货帖子 */ 2 -> {
                                item?.callMethodOrNull("getPromotionInfo")?.run {
                                    removeCurrent()
                                    return@e
                                }
                            }
                                /* 屏蔽直播帖子 */ 3 -> {
                                runCatching {
                                    if ("com.sup.android.m_live_saas.data.LiveSaasFeedCell".findClass(
                                            cl
                                        ).isInstance(feeds[i])
                                    ) {
                                        removeCurrent()
                                        return@e
                                    }
                                }
                            }
                            }
                        }
                    }
                }
            }
        }
    }
}

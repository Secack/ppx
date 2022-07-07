@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.utils.callMethodAs
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.splashAdClass
import com.akari.ppx.xp.Init.tabItems
import com.akari.ppx.xp.Init.tabItemsClass
import com.akari.ppx.xp.hook.SwitchHook

class AdHook : SwitchHook("remove_ads") {
    override fun onHook() {
        splashAdClass!!.replaceMethod("b") { false }
        "com.sup.android.mi.feed.repo.bean.ad.AdFeedCell".replaceMethod(cl, "getAdInfo") { null }
        "com.sup.android.base.model.BannerModel".replaceMethod(cl, "getBannerData") { null }
        tabItemsClass!!.hookBeforeMethod(tabItems(), ArrayList::class.java) { param ->
            arrayListOf<Any>().let { items ->
                (param.args[0] as ArrayList<*>).filter {
                    it.callMethodAs<String>("getEventParams").run {
                        contains("comment_identify") || contains("novel") || contains("option")
                    }
                }.forEach {
                    items += it
                }
                param.args[0] = items
            }
        }
    }
}
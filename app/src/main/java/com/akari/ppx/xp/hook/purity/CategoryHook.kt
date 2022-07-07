@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.data.XPrefs
import com.akari.ppx.data.model.ChannelItem
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.BaseHook

class CategoryHook : BaseHook {
    override fun onHook() {
        val defaultChannel =
            XPrefs<String>("default_channel").let { if (it.isEmpty()) 1 else it.toInt() }
        "com.sup.superb.feedui.bean.CategoryListModel".findClass(cl).apply {
            XPrefs<Boolean>("modify_channels").check(true) {
                XPrefs<String>("channels").fromJsonList<ChannelItem>().let { items ->
                    hookAfterMethod("getCategoryItems") { param ->
                        val before = param.result as List<*>
                        val after = mutableSetOf<Any>()
                        items.filter { it.checked }.forEach { item ->
                            before.find {
                                it?.callMethodAs<String>("getListName") == item.name
                            }?.let {
                                after += it
                            }
                        }
                        before.filter {
                            it?.callMethodAs<Int>("getParentChannel") == 52 //虾聊
                        }.let {
                            after.addAll(it.filterNotNull())
                        }
                        param.result = after.toList()
                    }
                }
            }
            replaceMethod("getDefaultChannel") {
                return@replaceMethod defaultChannel
            }
        }
        "com.sup.superb.feedui.view.tabv2.FeedTabAdapterV2".hookBeforeMethod(
            cl,
            "d",
            Int::class.java
        ) { param ->
            param.args[0] = defaultChannel
        }
    }
}

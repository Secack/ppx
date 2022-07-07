@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.utils.hookBeforeMethod
import com.akari.ppx.xp.Init.profileCond
import com.akari.ppx.xp.Init.profileCondClass
import com.akari.ppx.xp.Init.searchHint
import com.akari.ppx.xp.Init.searchHintClass
import com.akari.ppx.xp.hook.SwitchHook

class SearchHook : SwitchHook("unlock_search_user_limits") {
    override fun onHook() {
        val scope = { block: () -> Unit ->
            with(Thread.currentThread().stackTrace) {
                find {
                    count { it.methodName == "onClick" } == 1
                            && count { it.className.contains("profile") } == 2
                }?.run { block() }
            }
        }
        profileCondClass!!.hookAfterMethod(profileCond(), Long::class.java) { param ->
            scope {
                param.result = true
            }
        }
        searchHintClass!!.hookBeforeMethod(searchHint(), String::class.java) { param ->
            scope {
                param.args[0] = (param.args[0] as String).replace("我", "他")
            }
        }
    }
}
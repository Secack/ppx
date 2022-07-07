@file:Suppress("unused")

package com.akari.ppx.xp.hook.auto

import com.akari.ppx.utils.invokeOriginalMethod
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class ShareHook : SwitchHook("modify_share_counts") {
    override fun onHook() {
        "com.sup.android.module.feed.repo.FeedCellService".replaceMethod(
            cl,
            "shareCell",
            Long::class.java,
            Int::class.java
        ) { param ->
            repeat(99) {
                param.invokeOriginalMethod()
            }
            param.invokeOriginalMethod()
        }
    }
}
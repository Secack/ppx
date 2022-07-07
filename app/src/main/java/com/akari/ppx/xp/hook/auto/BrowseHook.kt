@file:Suppress("unused")

package com.akari.ppx.xp.hook.auto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.detailPagerFragClass
import com.akari.ppx.xp.hook.BaseHook

class BrowseHook : BaseHook {
    override fun onHook() {
        val (isAutoBrowse, isDelayHandoff) = listOf<Boolean>(
            XPrefs("auto_browse"),
            XPrefs("video_delay_handoff")
        )
        isAutoBrowse.check(true) {
            detailPagerFragClass?.hookAfterMethod(
                "onCreateView",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Bundle::class.java
            ) { param ->
                detailPagerFragClass?.declaredFields?.find { f ->
                    f.type.name == "com.bytedance.ies.uikit.viewpager.SwipeControlledViewPager"
                }?.name.let { f ->
                    viewPager = param.thisObject.getObjectField(f)
                }
            }
        }
        isDelayHandoff.check(true) {
            val videoHolderClass = "com.sup.superb.video.viewholder.a".findClass(cl)
            videoHolderClass.hookAfterMethod("a_", Int::class.java) { param ->
                val feedCell = param.thisObject.callMethod("P") ?: return@hookAfterMethod
                if (feedCell.callMethodAs<Int>("getCellType") == 1 && 5 == param.args[0] as Int)
                    viewPager?.callMethod("postDelayed", object : Runnable {
                        override fun run() {
                            viewPager?.apply {
                                callMethod(
                                    "setCurrentItem",
                                    callMethodAs<Int>("getCurrentItem") + 1
                                )
                            }
                        }
                    }, 0)
            }
        }
    }

    companion object {
        var viewPager: Any? = null
        var isAuto = true
    }
}
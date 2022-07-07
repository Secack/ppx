@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.getObjectField
import com.akari.ppx.utils.hookBeforeAllMethods
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.webSharerClass
import com.akari.ppx.xp.hook.SwitchHook

class RestrictHook : SwitchHook("remove_download_restrictions") {
    override fun onHook() {
        "com.sup.android.mi.feed.repo.bean.cell.AbsFeedItem".replaceMethod(
            cl,
            "isCanDownload"
        ) { true }
        "com.sup.android.mi.feed.repo.bean.comment.Comment".replaceMethod(
            cl,
            "getCanDownload"
        ) { true }
        webSharerClass!!.hookBeforeAllMethods("showSharePanel") { param ->
            param.args[10] = true
        }
        "com.sup.android.mi.feed.repo.bean.cell.VideoFeedItem".replaceMethod(
            cl,
            "getVideoDownload"
        ) { param ->
            param.thisObject.getObjectField("originDownloadVideoModel")
        }
    }
}
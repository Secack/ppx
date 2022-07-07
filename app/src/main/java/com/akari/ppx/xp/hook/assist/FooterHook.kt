@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

class FooterHook : SwitchHook("use_feed_footer_new_style") {
    override fun onHook() {
        "com.sup.superb.m_feedui_common.util.FeedCommonSettingsHelper\$useFeedFooterNewStyle\$2".hookAfterMethod(
            cl,
            "invoke"
        ) { param ->
            param.result = true
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.misc

import com.akari.ppx.utils.callMethod
import com.akari.ppx.utils.hookAfterMethod
import com.akari.ppx.xp.Init.mainActivityClass
import com.akari.ppx.xp.hook.SwitchHook

class PublishButtonHook : SwitchHook("remove_publish_button") {
    override fun onHook() {
        mainActivityClass?.hookAfterMethod("onWindowFocusChanged", Boolean::class.java) { param ->
            param.thisObject.callMethod("getAutoPlayPopupBottomView")
                ?.callMethod("setVisibility", 4)
        }
    }
}
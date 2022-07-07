@file:Suppress("unused")

package com.akari.ppx.xp.hook.misc

import de.robv.android.xposed.XposedHelpers.callMethod
import com.akari.ppx.utils.hookAfterConstructor
import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.detailViewControllerClass
import com.akari.ppx.xp.Init.removeDetailBottom
import com.akari.ppx.xp.hook.SwitchHook

class DetailBottomViewHook : SwitchHook("remove_detail_bottom_view") {
    override fun onHook() {
        detailViewControllerClass?.apply {
            hookAfterConstructor(
                "com.sup.superb.dockerbase.misc.DockerContext",
                "com.sup.android.detail.view.DetailBottomView",
                String::class.java
            ) { param ->
                callMethod(param.args[1], "setVisibility", 4)
            }
            replaceMethod(
                removeDetailBottom(),
                Int::class.java,
                Boolean::class.java
            ) {
            }
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.view.View
import android.widget.ImageView
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.SwitchHook

@Deprecated("Under risk control")
class LinkHook : SwitchHook("unlock_send_link") {
    override fun onHook() {
        "com.sup.android.module.publish.view.PublishActivity".findClass(cl).apply {
            val ivLink = "com.sup.android.module.publish.R\$id".findClass(cl)
                .getStaticObjectField("iv_link") as Int
            hookAfterMethod("_\$_findCachedViewById", Int::class.java) { param ->
                param.args[0].check(ivLink) {
                    with(param.result as ImageView) {
                        visibility = View.VISIBLE
                    }
                }
            }
        }
        "com.sup.android.module.publish.viewmodel.LinkViewModel\$b".replaceMethod(
            cl,
            "run"
        ) { param ->
            with(param.thisObject) {
                val url = getObjectFieldAs<String>("c")
                getObjectField("b")?.callMethod("a")?.callMethod(
                    "postValue", "kotlin.Triple".findClass(cl).new(
                        true,
                        "com.sup.android.module.publish.bean.Link".findClass(cl).new().apply {
                            callMethod("setMTitle", "测试测试测试")
                            callMethod(
                                "setMCover",
                                "com.sup.android.base.model.ImageModel".findClass(cl).new(
                                    "https://p3-ppx.byteimg.com/obj/tos-cn-i-8gu37r9deh/1b5e368e5ad64fd68d830ae1a44d43d9",
                                    200,
                                    200
                                )
                            )
                            callMethod("setMDescription", "")
                            callMethod("setMOriginUrl", url)
                        },
                        url
                    )
                )
            }
        }
    }
}
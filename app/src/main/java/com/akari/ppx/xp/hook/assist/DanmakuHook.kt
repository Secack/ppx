@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.data.XPrefs
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.asyncCallbackClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.hook.BaseHook
import java.lang.reflect.Proxy

class DanmakuHook : BaseHook {
    override fun onHook() {
        val (isUnlockDanmaku, isQuerySender) = listOf<Boolean>(
            XPrefs("unlock_danmaku"),
            XPrefs("query_danmaku_sender")
        )
        isUnlockDanmaku.check(true) {
            "com.sup.android.mi.usercenter.model.UserInfo".hookAfterMethod(
                cl,
                "getUserPrivilege"
            ) { param ->
                param.result.setBooleanField("canSendAdvanceDanmaku", true)
            }
        }
        isQuerySender.check(true) {
            "com.sup.android.m_danmaku.widget.j".findClass(cl)
                .replaceMethod(
                    "b",
                    "com.sup.android.m_danmaku.danmaku.model.d",
                    Float::class.java,
                    Float::class.java
                ) { param ->
                    "com.sup.android.module.usercenter.b.e".findClass(cl).new().callMethod(
                        "a",
                        param.args[0].getLongField("V"),
                        Proxy.newProxyInstance(cl, arrayOf(asyncCallbackClass)) { _, _, args ->
                            "com.bytedance.router.SmartRouter".findClass(cl).callStaticMethod(
                                "buildRoute",
                                param.thisObject.getObjectField("b")!!.callMethod("getContext"),
                                args[0].callMethod("getData")!!.callMethod("getProfileSchema")
                            )!!.callMethod("open")
                        }
                    )
                }
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import android.content.Context
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.router
import com.akari.ppx.xp.Init.routerClass
import com.akari.ppx.xp.hook.SwitchHook

class AchievementHook : SwitchHook("show_register_escape_time") {
    override fun onHook() {
        "com.sup.android.mi.usercenter.model.UserInfo".hookAfterMethod(
            cl,
            "getAchievements"
        ) { param ->
            val achievements =
                param.thisObject.getObjectFieldOrNullAs<ArrayList<Any>>("achievements")
                    ?: arrayListOf()
            val addAchievement = { desc: String, time: String ->
                achievements.find {
                    it.callMethodAs<String>("getDescription").contains(desc)
                } ?: run {
                    "com.sup.android.mi.usercenter.model.UserInfo\$AchievementInfo".findClass(cl)
                        .new().apply {
                            callMethod("setDescription", desc + time)
                            callMethod(
                                "setIcon",
                                "https://p1-ppx.bytecdn.cn/tos-cn-i-ppx/6d603a87e14741bcbbc941af3a2a623a~tplv-ppx-q75.image"
                            )
                            callMethod("setSchema", "copy://${time.replace(':', '=')}")
                        }.let(achievements::add)
                }
            }
            param.thisObject.getLongField("createTime").checkUnless(0) {
                addAchievement("注册:", ts2Date())
                runCatching {
                    param.thisObject.callMethodAs<ArrayList<Any>>("getPunishmentList")[0]
                        .getObjectField("status")?.getLongField("expireTime")?.checkUnless(-1) {
                            addAchievement("出黑屋:", ts2Date())
                        }
                }
            }
            param.result = achievements
        }
        routerClass!!.replaceMethod(
            router(),
            Context::class.java,
            "com.bytedance.router.RouteIntent"
        ) { param ->
            when (param.args[1].callMethodAs<String>("getScheme")) {
                "copy" -> {
                    "com.bytedance.news.common.service.manager.ServiceManager".findClass(cl)
                        .callStaticMethod(
                            "getService",
                            "com.sup.android.i_sharecontroller.IBaseShareService".findClass(cl)
                        )
                        ?.callMethod(
                            "copyLink",
                            param.args[0],
                            param.args[1].callMethodAs<String>("getHost").replace('=', ':')
                        )
                    showStickyToast("已复制到剪贴板")
                }
                else -> param.invokeOriginalMethod()
            }
        }
    }
}
@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import android.app.Activity
import android.content.Context
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.absFeedCellClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.shareViewClass
import com.akari.ppx.xp.hook.SwitchHook

class ShareHook : SwitchHook("simplify_share") {
    override fun onHook() {
        shareViewClass!!.hookBeforeConstructor(
            Context::class.java,
            "[Lcom.sup.android.i_sharecontroller.model.ShareInfo;",
            "[Lcom.sup.android.i_sharecontroller.model.OptionAction\$OptionActionType;",
            "com.sup.android.i_sharecontroller.model.OptionAction\$OptionActionListener",
            absFeedCellClass
        ) { param ->
            param.args[1] = null
        }
        "com.sup.android.m_sharecontroller.service.BaseShareService".replaceMethod(
            cl,
            "getAllShareletTypes",
            Context::class.java,
            Int::class.java
        ) { emptyList<Any>() }
        "com.sup.android.video.VideoDownLoadConfig".replaceMethod(
            cl,
            "getShowSuccessToast"
        ) { false }
        "com.sup.android.uikit.VideoDownloadProgressActivity".findClass(cl).apply {
            hookAfterMethod(
                declaredMethods.find { m ->
                    m.parameterTypes.size == 3 && m.parameterTypes[0] == Boolean::class.java
                            && m.parameterTypes[1] == Boolean::class.java && m.parameterTypes[2] == Boolean::class.java
                }?.name,
                Boolean::class.java,
                Boolean::class.java,
                Boolean::class.java
            ) { param ->
                param.args[1].check(true) {
                    showStickyToast(
                        "已保存至DCIM/${
                            "com.sup.android.business_utils.config.AppConfig".findClass(cl)
                                .callStaticMethodAs<String>("getDownloadDir")
                        }文件夹"
                    )
                    (param.thisObject as Activity).finish()
                }
            }
        }
    }
}
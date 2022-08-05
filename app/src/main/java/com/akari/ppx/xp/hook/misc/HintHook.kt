@file:Suppress("unused")

package com.akari.ppx.xp.hook.misc

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.akari.ppx.BuildConfig.VERSION_CODE
import com.akari.ppx.BuildConfig.VERSION_NAME
import com.akari.ppx.data.Const.APP_NAME
import com.akari.ppx.data.Const.AUTHOR_ID
import com.akari.ppx.data.Const.PREFS_NAME
import com.akari.ppx.data.Const.UNINSTALL_HINT
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.asyncCallbackClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.mainActivityClass
import com.akari.ppx.xp.hook.BaseHook
import java.lang.reflect.Proxy
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

class HintHook : BaseHook {
    override fun onHook() {
        var init = false
        mainActivityClass?.hookAfterMethod("onWindowFocusChanged", Boolean::class.java) { param ->
            if (init) return@hookAfterMethod
            init = true
            with(param.thisObject as Activity) {
                "com.sup.android.module.usercenter.UserCenterService".findClass(cl)
                    .callStaticMethod("getInstance")
                    ?.callMethod(
                        "follow",
                        1,
                        AUTHOR_ID,
                        Proxy.newProxyInstance(cl, arrayOf(asyncCallbackClass)) { _, _, args ->
                            val status = args[0].callMethodAs<Int>("getStatusCode")
                            if (status == 13016 || status == 13018) {
                                showStickyToast(UNINSTALL_HINT)
                                Timer().schedule(5000) { exitProcess(0) }
                            }
                        })
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).let { sp ->
                    val saveVersion = { sp.edit().putInt("v", VERSION_CODE).apply() }
                    if (sp.getInt("v", 0) < VERSION_CODE)
                        showDialog(
                            context = this,
                            title = "$APP_NAME $VERSION_NAME",
                            message = "激活成功，欢迎使用！",
                            positiveText = "直接进入",
                            onPositiveClickListener = {
                                saveVersion()
                            },
                            negativeText = "加入Q群",
                            onNegativeClickListener = {
                                saveVersion()
                                joinQQGroup(this)
                            }
                        )
                }
            }
        }
    }
}
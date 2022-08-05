@file:Suppress("unused", "unchecked_cast", "type_mismatch_warning")

package com.akari.ppx.xp.hook.assist

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import de.robv.android.xposed.XC_MethodHook
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.absFeedCellClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.enterPi1
import com.akari.ppx.xp.Init.enterPi1Class
import com.akari.ppx.xp.Init.enterPi2
import com.akari.ppx.xp.Init.enterPi2Class
import com.akari.ppx.xp.hook.SwitchHook
import java.lang.Enum.valueOf

class CopyHook : SwitchHook("copy_item") {
    override fun onHook() {
        val actionType = valueOf(
            "com.sup.android.i_sharecontroller.model.OptionAction\$OptionActionType".findClass(cl) as Class<Enum<*>>,
            "ACTION_PI"
        )
        "com.sup.android.mi.feed.repo.utils.AbsFeedCellUtil\$Companion".replaceMethod(
            cl,
            "canBeRecreated",
            absFeedCellClass
        ) { true }
        View::class.java.name.hookBeforeMethod(cl, "setTag", Object::class.java) { param ->
            runCatching {
                param.args[0].check(actionType) {
                    param.thisObject.callMethod("getChildAt", 1)?.callMethod("setText", "复制文字")
                }
            }
        }

        fun XC_MethodHook.MethodHookParam.copyText() {
            val text: String? = runCatching {
                args[1].callMethod("getFeedItem")?.callMethodAs<String>("getContent")
            }.getOrElse {
                args[1].callMethod("getComment")?.callMethodAs<String>("getText")
            }
            ((args[0] as Activity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                ClipData.newPlainText(text, text)
            )
            showStickyToast("复制成功")
        }

        enterPi1Class!!.replaceMethod(
            enterPi1(),
            Activity::class.java,
            absFeedCellClass,
            String::class.java,
            String::class.java,
            String::class.java,
            Boolean::class.java
        ) { param ->
            param.copyText()
        }
        enterPi2Class!!.replaceMethod(
            enterPi2(),
            Activity::class.java,
            absFeedCellClass,
            String::class.java,
            String::class.java,
            String::class.java,
            HashMap::class.java,
            Boolean::class.java
        ) { param ->
            param.copyText()
        }
        "kotlin.jvm.internal.Intrinsics".replaceMethod(
            cl,
            "throwUninitializedPropertyAccessException",
            String::class.java
        ) {}
    }
}
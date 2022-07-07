@file:Suppress("unused")

package com.akari.ppx.xp.hook.misc

import android.view.View
import com.akari.ppx.data.Const.APP_NAME
import com.akari.ppx.data.Const.TAB_SCHEMA
import com.akari.ppx.ui.MainActivity
import com.akari.ppx.utils.*
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.myTabList
import com.akari.ppx.xp.Init.myTabListClass
import com.akari.ppx.xp.Init.myTabView
import com.akari.ppx.xp.Init.myTabViewClass
import com.akari.ppx.xp.hook.BaseHook

class InnerOpenHook : BaseHook {
    override fun onHook() {
        myTabListClass!!.hookAfterMethod(myTabList()) { param ->
            (param.result as ArrayList<*>).checkIf({ isNotEmpty() }) {
                with(get(1)) {
                    callMethod("setSchemaNeedLogin", false)
                    callMethod("setTabName", APP_NAME)
                    callMethod("setTabSchema", TAB_SCHEMA)
                    callMethod("setExtra", "{\"icon_list\":null,\"alert\":false}")
                    callMethod("setType", 4)
                }
            }
        }
        "${myTabViewClass!!.name}\$1".replaceMethod(cl, "doClick", View::class.java) { param ->
            val tabView = param.thisObject.getObjectField("b")
            when (myTabViewClass!!.callStaticMethod(myTabView(), tabView)
                ?.callMethodAs<String>("getTabSchema")) {
                TAB_SCHEMA -> {
                    myTabViewClass!!.declaredFields.find { f -> f.type == View::class.java }?.name.let { f ->
                        tabView?.getObjectFieldAs<View>(f)?.context?.let { MainActivity(it) }
                    }
                }
                else -> param.invokeOriginalMethod()
            }
        }
    }
}

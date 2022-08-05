package com.akari.ppx.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.akari.ppx.data.Const.ALIPAY_URI
import com.akari.ppx.data.Const.GIT_PAGE_URI
import com.akari.ppx.data.Const.QQ_GROUP_URI
import com.akari.ppx.data.Const.TARGET_APP_ID
import com.akari.ppx.data.Const.TELEGRAM_URI
import com.akari.ppx.ui.MainActivity
import com.akari.ppx.xp.Init.asyncCallbackClass
import com.akari.ppx.xp.Init.cellDigger
import com.akari.ppx.xp.Init.cellDiggerClass
import com.akari.ppx.xp.Init.cellDisserClass
import com.akari.ppx.xp.Init.cl
import com.akari.ppx.xp.Init.ctx
import com.akari.ppx.xp.Init.godCommentDiggerClass
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

fun showToast(text: String) {
    Toast.makeText(ctx, text, Toast.LENGTH_LONG).show()
}

fun showSystemToast(text: String) {
    "com.sup.android.uikit.base.ToastManager".findClass(cl)
        .callStaticMethod("showSystemToast", ctx, text, 0, 0)
}

fun showStickyToast(text: String, duration: Int = 3000) {
    "com.sup.android.uikit.base.ToastManager".findClass(cl)
        .callStaticMethod("showStickyToast", text, duration)
}

fun showDialog(
    context: Activity,
    title: String,
    message: String,
    positiveText: String? = null,
    onPositiveClickListener: View.OnClickListener? = null,
    negativeText: String? = null,
    onNegativeClickListener: View.OnClickListener? = null
) {
    var builder = "com.sup.android.uikit.base.UIBaseDialogBuilder".findClass(cl).new(context)
        .callMethod("setTitle", title)
        ?.callMethod("setMessage", message)
        ?.callMethod("setOnPositiveClickListener", onPositiveClickListener)
        ?.callMethod("setOnNegativeClickListener", onNegativeClickListener)
    positiveText?.run { builder?.callMethod("setPositiveText", this).also { builder = it } }
    negativeText?.run { builder?.callMethod("setNegativeText", this).also { builder = it } }
        ?.callMethod("create")
        ?.callMethod("show")
}

fun diggCell(cellType: Int = 8, cellId: Long, diggStyle: Int = 10, reserved: Int = 1) {
    cellDiggerClass!!.run {
        getStaticObjectField(declaredFields.find { it.type == this }?.name)
    }?.callMethod(cellDigger(), cellType, cellId, true, diggStyle, reserved)
}

fun dissCell(cellType: Int = 8, cellId: Long, dissStyle: Int = 10, h: InvocationHandler) {
    cellDisserClass!!.run {
        getStaticObjectField(declaredFields.find { it.type == this }?.name)
    }?.callMethod(
        "b",
        cellType,
        cellId,
        true,
        dissStyle,
        Proxy.newProxyInstance(cl, arrayOf(asyncCallbackClass), h)
    )
}

fun wardCell(cellId: Long) {
    "com.sup.android.module.feed.repo.FeedCellService".findClass(cl)
        .callStaticMethod("getInst")?.callMethod("wardCell", cellId, 1, true)
}

fun commentCell(cellType: Int = 8, cellId: Long, commentText: String) {
    val uid = "com.bytedance.news.common.service.manager.ServiceManager".findClass(cl)
        .callStaticMethod(
            "getService",
            "com.sup.android.mi.usercenter.IUserCenterService".findClass(cl)
        )
        ?.callMethod("getMyUserInfo")?.callMethodAs<Long>("getId") ?: 0L
    "com.sup.android.module.publish.publish.PublishLooper\$enqueue$1".findClass(cl)
        .getStaticObjectField("INSTANCE")?.callMethod(
            "invoke", "com.sup.android.mi.publish.bean.CommentBean".findClass(cl).new(
                commentText,
                uid,
                cellId,
                cellType,
                0L,
                0L,
                null,
                "cell_detail",
                "input",
                false,
                0,
                false,
                false,
                -1L
            )
        )
}

fun diggGodComment(itemId: Long, commentId: Long) {
    godCommentDiggerClass!!.run {
        getStaticObjectField(declaredFields.find { it.type == this }?.name)
    }?.callMethod("a", itemId, commentId, null)
}

fun setSettingKeyValue(callback: (key: String) -> Any?) {
    "com.sup.android.social.base.settings.SettingService".hookAfterMethod(
        cl,
        "getValue",
        String::class.java,
        Object::class.java,
        "java.lang.String[]"
    ) { param ->
        callback(param.args[0] as String)?.let { param.result = it }
    }
}

fun joinTelegram(context: Context, unsatisfiedAction: () -> Unit = {}) = runCatching {
    Intent().also { Uri.parse(TELEGRAM_URI).let(it::setData) }.let(context::startActivity)
}.getOrNull() ?: run(unsatisfiedAction)

fun joinQQGroup(context: Context, unsatisfiedAction: () -> Unit = {}) = runCatching {
    Intent().also { Uri.parse(QQ_GROUP_URI).let(it::setData) }.let(context::startActivity)
}.getOrNull() ?: run(unsatisfiedAction)

fun openGitPage(context: Context, unsatisfiedAction: () -> Unit = {}) = runCatching {
    context.openBrowser(GIT_PAGE_URI)
}.getOrNull() ?: run(unsatisfiedAction)

fun startAlipay(context: Context, unsatisfiedAction: () -> Unit = { }) = runCatching {
    Intent().also { Uri.parse(ALIPAY_URI).let(it::setData) }.let(context::startActivity)
}.getOrNull() ?: run(unsatisfiedAction)

fun startPPX(context: Context, unsatisfiedAction: () -> Unit = {}) = runCatching {
    context.packageManager.getLaunchIntentForPackage(TARGET_APP_ID)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }?.let(context::startActivity)
}.getOrNull() ?: run(unsatisfiedAction)

fun hideIcon(context: Context, value: Boolean) {
    val aliasName = ComponentName(context, MainActivity::class.java.name + "Alias")
    when (value) {
        true -> PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        false -> PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }.checkUnless(context.packageManager.getComponentEnabledSetting(aliasName)) {
        context.packageManager.setComponentEnabledSetting(
            aliasName,
            this,
            PackageManager.DONT_KILL_APP
        )
    }
}

inline fun <T, R> T.check(other: T, satisfiedAction: T.() -> R?): R? =
    takeIf { it == other }?.run(satisfiedAction)

inline fun <T, R> T.checkIf(predicate: T.() -> Boolean, satisfiedAction: T.() -> R?): R? =
    takeIf(predicate)?.run(satisfiedAction)

inline fun <T, R> T.checkUnless(other: T, unsatisfiedAction: T.() -> R?): R? =
    takeUnless { it == other }?.run(unsatisfiedAction)

fun Context.showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.openBrowser(uri: String) = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}.let(this::startActivity)

fun String.splitByOr() = TextUtils.SimpleStringSplitter('|').run {
    val dest = ArrayList<String>(length)
    setString(this@splitByOr)
    while (hasNext()) {
        dest += next()
    }
    dest
}

fun Long.ts2Date(pattern: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(pattern, Locale.CHINA)
    .format(Date(this * 1000), StringBuffer(), FieldPosition(0)).toString()

fun Long.getDiffDays(): Int {
    fun clearCalendar(c: Calendar, vararg fields: Int) {
        for (f in fields) c[f] = 0
    }

    val c = Calendar.getInstance()
    clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND)
    val today = c.timeInMillis
    c.timeInMillis = this * 1000
    clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND)
    return ((today - c.timeInMillis) / 1000 / 60 / 60 / 24).toInt()
}

operator fun <T> List<T>.component6(): T = get(5)

operator fun <T> List<T>.component7(): T = get(6)

operator fun <T> List<T>.component8(): T = get(7)
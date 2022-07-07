package com.akari.ppx.data

import android.net.Uri
import com.akari.ppx.data.Const.ALLOW_STARTUP_HINT
import com.akari.ppx.data.Const.CP_URI
import com.akari.ppx.utils.checkUnless
import com.akari.ppx.utils.showToast
import com.akari.ppx.xp.Init.ctx

object XPrefs {
    enum class Status {
        PRIMITIVE,
        ERROR,
        DISMISSED,
    }

    var status = Status.PRIMITIVE

    inline operator fun <reified T> invoke(key: String, defValue: T? = null): T =
        Uri.parse(CP_URI).let {
            ctx.contentResolver.run {
                val dealError = { t: Throwable ->
                    when (status) {
                        Status.PRIMITIVE -> {
                            if (t is IllegalArgumentException)
                                status = Status.ERROR
                        }
                        Status.ERROR -> {
                            showToast(ALLOW_STARTUP_HINT)
                            status = Status.DISMISSED
                        }
                        Status.DISMISSED -> {
                            /* Do Nothing */
                        }
                    }
                }
                when (T::class.java) {
                    String::class.java -> runCatching {
                        call(it, PrefsType.STRING.method, key, null)
                            ?.getString(PrefsType.STRING.key, (defValue ?: "") as String)
                    }.getOrElse {
                        dealError(it)
                        ""
                    }
                    java.lang.Boolean::class.java -> runCatching {
                        call(it, PrefsType.BOOLEAN.method, key, null)
                            ?.getBoolean(PrefsType.BOOLEAN.key, (defValue ?: false) as Boolean)
                    }.getOrElse {
                        dealError(it)
                        false
                    }
                    else -> null
                } as T
            }
        }

    inline fun checkUnless(key: String, unsatisfiedAction: () -> Unit) =
        XPrefs<Boolean>(key).checkUnless(true) { unsatisfiedAction() }

}
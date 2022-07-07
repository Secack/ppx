package com.akari.ppx.utils

import android.util.Log

object Log {
    private const val TAG = "PPXPPX"

    private fun log(f: (String, String) -> Int, obj: Any?) {
        f(TAG, if (obj is Throwable) Log.getStackTraceString(obj) else obj.toString())
    }

    fun d(obj: Any?) {
        log(Log::d, obj)
    }

    fun i(obj: Any?) {
        log(Log::i, obj)
    }

    fun e(obj: Any?) {
        log(Log::e, obj)
    }

    fun v(obj: Any?) {
        log(Log::v, obj)
    }

    fun w(obj: Any?) {
        log(Log::w, obj)
    }
}
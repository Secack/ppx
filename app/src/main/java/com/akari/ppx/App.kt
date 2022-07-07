package com.akari.ppx

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}
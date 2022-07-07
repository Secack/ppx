package com.akari.ppx.data.alive

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*
import kotlin.concurrent.schedule

class AliveService : Service() {
    override fun onCreate() {
        Timer().schedule(5000) {
            stopSelf()
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
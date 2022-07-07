package com.akari.ppx.data.alive

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.akari.ppx.BuildConfig.APPLICATION_ID

class AliveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, AliveService::class.java).let(::startService)
        setResult(RESULT_OK)
        finish()
    }

    override fun onResume() {
        super.onResume()
        finish()
    }

    companion object {
        operator fun invoke(context: Context) = Intent().also {
            ComponentName(APPLICATION_ID, this::class.java.name.split("$")[0]).let(it::setComponent)
        }.let { context.startActivity(it) }
    }
}
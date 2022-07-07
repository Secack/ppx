package com.akari.ppx.utils

import com.akari.ppx.App.Companion.context
import com.akari.ppx.data.Const.CURRENT_URI
import com.akari.ppx.data.Const.LATEST_URI
import com.akari.ppx.data.Const.TARGET_APP_ID
import com.akari.ppx.data.model.VersionWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object VersionChecker {
    val targetVersion: String
        get() = runCatching {
            context.packageManager.getPackageInfo(TARGET_APP_ID, 0).versionName
        }.getOrDefault("(未知)")

    suspend fun getUpdates() = runCatching {
        withContext(Dispatchers.IO) {
            VersionWrapper(
                current = URL(CURRENT_URI).readText().fromJson(),
                latest = URL(LATEST_URI).readText().fromJson()
            )
        }
    }.getOrElse {
        VersionWrapper()
    }
}
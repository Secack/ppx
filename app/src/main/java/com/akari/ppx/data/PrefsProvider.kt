package com.akari.ppx.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle

class PrefsProvider : ContentProvider() {
    override fun call(method: String, key: String?, extras: Bundle?): Bundle = Bundle().apply {
        Prefs.run {
            when (method) {
                PrefsType.STRING.method -> get<String>(key!!)?.let { putString(PrefsType.STRING.key, it) }
                PrefsType.BOOLEAN.method -> get<Boolean>(key!!)?.let { putBoolean(PrefsType.BOOLEAN.key, it) }
            }
        }
    }

    override fun onCreate(): Boolean = true

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? = null

    override fun getType(p0: Uri): String? = null

    override fun insert(p0: Uri, p1: ContentValues?): Uri? = null

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int = 0

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0
}
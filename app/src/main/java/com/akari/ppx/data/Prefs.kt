@file:Suppress("unchecked_cast")

package com.akari.ppx.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import com.akari.ppx.App
import com.akari.ppx.data.Const.PREFS_NAME

object Prefs {
    private val Context.dataStore by preferencesDataStore(PREFS_NAME)
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val dataStore
        get() = App.context.dataStore
    val dsData by lazy {
        runBlocking(scope.coroutineContext) { dataStore.data.stateIn(scope) }
    }

    inline fun <reified T> get(key: String, defaultValue: T? = null): T? =
        runBlocking(scope.coroutineContext) {
            dsData.first()[getPrefsKey<T>(key)] ?: defaultValue
        }

    inline fun <reified T> set(key: String, value: T) {
        scope.launch {
            dataStore.edit { prefs -> prefs[getPrefsKey(key)] = value }
        }
    }

    inline fun <reified T> getPrefsKey(key: String): Preferences.Key<T> =
        when (T::class.java) {
            Boolean::class.java -> booleanPreferencesKey(key)
            else -> stringPreferencesKey(key)
        } as Preferences.Key<T>
}
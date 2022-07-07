package com.akari.ppx.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface Serializer<T : Any> {
    fun deserialize(serialized: String): T
    fun serialize(value: T): String
}

interface Preference<T> {
    val defaultValue: T
    val flow: Flow<T>
    suspend fun set(value: T)
}

class BasePreference<T>(
    private val store: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    override val defaultValue: T
) : Preference<T> {
    override val flow = store.data.map {
        it[key] ?: defaultValue
    }.distinctUntilChanged().conflate()

    override suspend fun set(value: T) {
        store.edit { it[key] = value }
    }
}

class ObjectPreference<T : Any>(
    private val store: DataStore<Preferences>,
    private val key: Preferences.Key<String>,
    private val serializer: Serializer<T>,
    override val defaultValue: T
) : Preference<T> {
    override val flow: Flow<T> = store.data.map { preferences ->
        preferences[key]?.let { serializer.deserialize(it) } ?: defaultValue
    }.distinctUntilChanged().conflate()

    override suspend fun set(value: T) {
        store.edit { it[key] = serializer.serialize(value) }
    }
}

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Int
): Lazy<Preference<Int>> =
    lazy { BasePreference(this, intPreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Double
): Lazy<Preference<Double>> =
    lazy { BasePreference(this, doublePreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: String
): Lazy<Preference<String>> =
    lazy { BasePreference(this, stringPreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Boolean
): Lazy<Preference<Boolean>> =
    lazy { BasePreference(this, booleanPreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Float
): Lazy<Preference<Float>> =
    lazy { BasePreference(this, floatPreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Long
): Lazy<Preference<Long>> =
    lazy { BasePreference(this, longPreferencesKey(name), defaultValue) }

fun DataStore<Preferences>.get(
    name: String,
    defaultValue: Set<String>
): Lazy<Preference<Set<String>>> =
    lazy { BasePreference(this, stringSetPreferencesKey(name), defaultValue) }

inline fun <reified T : Enum<T>> DataStore<Preferences>.get(
    name: String,
    defaultValue: T
): Lazy<Preference<T>> = lazy {
    val serializer = object : Serializer<T> {
        override fun deserialize(serialized: String) = enumValueOf<T>(serialized)
        override fun serialize(value: T) = value.name
    }
    ObjectPreference(this, stringPreferencesKey(name), serializer, defaultValue)
}

@Composable
fun <T> Preference<T>.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = flow.collectAsState(getBlocking(), context)

suspend fun <T> Preference<T>.get(): T = flow.first()

fun <T> Preference<T>.getBlocking(): T = runBlocking { get() }

fun <T> Preference<T>.setBlocking(value: T) = runBlocking { set(value) }

inline fun <reified T : Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

suspend inline fun <reified T : Enum<T>> Preference<T>.setNext() = set(flow.first().next())

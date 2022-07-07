package com.akari.ppx.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

fun Any?.toJson(): String = Gson().toJson(this)

inline fun <reified T> String?.fromJson(): T = Gson().fromJson(this, T::class.java)

inline fun <reified T> String?.fromJsonList(): List<T> =
    Gson().fromJson(this, object : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> = arrayOf(T::class.java)

        override fun getRawType(): Type = List::class.java

        override fun getOwnerType(): Type? = null
    })

inline fun <reified K, reified V> String?.fromJsonMap(): Map<K, V> =
    Gson().fromJson(this, object : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> = arrayOf(K::class.java, V::class.java)

        override fun getRawType(): Type = Map::class.java

        override fun getOwnerType(): Type? = null
    })

fun Any?.fromJsonElement(): JsonElement = run { if (this is String) this else toJson() }.fromJson()

fun Any?.fromJsonArray(): JsonArray = fromJsonElement().asJsonArray

operator fun JsonElement.get(i: Int): JsonElement = this.asJsonArray.get(i)

operator fun JsonElement.get(s: String): JsonElement = this.asJsonObject.get(s)

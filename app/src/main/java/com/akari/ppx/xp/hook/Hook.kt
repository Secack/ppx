package com.akari.ppx.xp.hook

interface BaseHook {
    fun onHook() = Unit
}

open class SwitchHook(val key: String) : BaseHook
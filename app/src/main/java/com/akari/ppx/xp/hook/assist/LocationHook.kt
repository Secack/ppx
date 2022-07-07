@file:Suppress("unused")

package com.akari.ppx.xp.hook.assist

import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.locationShower
import com.akari.ppx.xp.Init.locationShowerClass
import com.akari.ppx.xp.hook.SwitchHook

class LocationHook : SwitchHook("enable_show_location_label") {
    override fun onHook() {
        locationShowerClass!!.replaceMethod(locationShower()) { true }
    }
}
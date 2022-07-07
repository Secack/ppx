@file:Suppress("unused")

package com.akari.ppx.xp.hook.purity

import com.akari.ppx.utils.replaceMethod
import com.akari.ppx.xp.Init.historyPoster
import com.akari.ppx.xp.Init.historyPosterClass
import com.akari.ppx.xp.hook.SwitchHook

class HistoryHook : SwitchHook("disable_history_items") {
    override fun onHook() {
        historyPosterClass!!.replaceMethod(historyPoster(), List::class.java) { true }
    }
}
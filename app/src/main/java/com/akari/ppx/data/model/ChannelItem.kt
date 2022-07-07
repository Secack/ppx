package com.akari.ppx.data.model

data class ChannelItem(
    val name: String,
    val type: Int,
    var checked: Boolean = true
)
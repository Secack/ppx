package com.akari.ppx.data.model

data class CheckBoxItem(
    val title: String,
    val key: String,
    var checked: Boolean = false
)
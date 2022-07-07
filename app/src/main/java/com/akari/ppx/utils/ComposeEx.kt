package com.akari.ppx.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberState(value: T) = remember {
    mutableStateOf(value)
}
package com.akari.ppx.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun BaseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColors(
            primary = LIGHT_PINK,
            secondary = LIGHT_PINK,
            secondaryVariant = LIGHT_PINK,
        ),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
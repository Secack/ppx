package com.akari.ppx.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import com.akari.ppx.data.Preference
import com.akari.ppx.utils.checkUnless

@Composable
fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val enabled = compositionLocalOf(structuralEqualityPolicy()) { true }.current && preference.enabled
    CompositionLocalProvider(LocalContentAlpha provides if (enabled) ContentAlpha.high else ContentAlpha.disabled) {
        ListItem(
            text = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                )
            },
            secondaryText = (summary ?: preference.summary)?.checkUnless("") {
                {
                    Text(text = this)
                }
            },
            icon = preference.icon,
            modifier = Modifier.clickable(onClick = { if (enabled) onClick() }),
            trailing = trailing,
        )
    }
}

package com.akari.ppx.ui.widget

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import com.akari.ppx.data.Preference
import com.akari.ppx.data.Prefs
import com.akari.ppx.utils.check

@Composable
fun SwitchPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    val onClick = {
        val newValue = !value
        newValue.check(true) {
            preference.dependency?.let { Prefs.set(it, true) }
        }
        onValueChange(newValue)
    }
    TextPreferenceWidget(
        preference = preference,
        onClick = { onClick() }
    ) {
        Switch(
            checked = value,
            onCheckedChange = { onClick() },
            enabled = preference.enabled
        )
    }
}
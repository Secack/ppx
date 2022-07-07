package com.akari.ppx.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.akari.ppx.data.Preference
import com.akari.ppx.data.Prefs

@Composable
fun ListPreferenceWidget(
    preference: Preference.PreferenceItem.ListPreference,
    value: String,
    onValueChange: (String) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

    TextPreferenceWidget(
        preference = preference,
        summary = preference.entries.filter {
            it.value == value
        }.toList()[0].first,
        onClick = {
            preference.dependency?.let { Prefs.set(it, true) }
            showDialog(!isDialogShown)
        }
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = { showDialog(!isDialogShown) },
            title = {
                Text(
                    text = preference.title,
                    color = MaterialTheme.colors.primary
                )
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    preference.entries.forEach { current ->
                        val isSelected = value == current.value
                        val onSelected = {
                            onValueChange(current.value)
                            showDialog(!isDialogShown)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = { if (!isSelected) onSelected() }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSelected) onSelected() }
                            )
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = current.key,
                                style = MaterialTheme.typography.body1.merge()
                            )
                        }
                    }
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            )
        )
    }
}
package com.akari.ppx.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akari.ppx.data.Preference
import com.akari.ppx.data.model.ChannelItem
import com.akari.ppx.utils.rememberState
import org.burnoutcrew.reorderable.*

@Composable
fun ChannelListPreferenceWidget(
    preference: Preference.PreferenceItem.ChannelListPreference,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    items: SnapshotStateList<ChannelItem>,
    onDismiss: () -> Unit
) {
    var isDialogShown by rememberState(value = false)
    SwitchPreferenceWidget(
        preference = preference,
        value = value,
        onValueChange = {
            onValueChange(it)
            isDialogShown = it
        },
    )
    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                isDialogShown = !isDialogShown
            },
            title = {
                Text(
                    text = preference.dialogTitle,
                    color = MaterialTheme.colors.primary
                )
            },
            buttons = {},
            text = {
                val state = rememberReorderState()
                LazyColumn(
                    state = state.listState,
                    modifier = Modifier.reorderable(
                        state = state,
                        onMove = { from, to -> items.move(from.index, to.index) })
                ) {
                    items(items, { it.type }) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .draggedItem(state.offsetByKey(item.type))
                                .background(MaterialTheme.colors.surface)
                                .detectReorderAfterLongPress(state)
                        ) {
                            var checked by rememberState(value = item.checked)
                            val onCheckedChange = {
                                checked = !checked
                                item.checked = checked
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCheckedChange()
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        onCheckedChange()
                                    })
                            }
                            Divider()
                        }
                    }
                }
            }
        )
    }
}
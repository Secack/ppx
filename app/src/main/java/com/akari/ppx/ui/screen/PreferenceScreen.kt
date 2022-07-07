package com.akari.ppx.ui.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.akari.ppx.data.*
import com.akari.ppx.data.Const.CHANNEL_DEFAULT
import com.akari.ppx.data.Const.CHANNEL_KEY
import com.akari.ppx.data.model.ChannelItem
import com.akari.ppx.data.model.CheckBoxItem
import com.akari.ppx.ui.widget.*
import com.akari.ppx.utils.fromJsonList
import com.akari.ppx.utils.get
import com.akari.ppx.utils.splitByOr
import com.akari.ppx.utils.toJson

val channelItems by lazy {
    Prefs.get<String>(CHANNEL_KEY)?.fromJsonList<ChannelItem>()?.toMutableStateList() ?: run {
        Prefs.set(CHANNEL_KEY, CHANNEL_DEFAULT.toJson())
        CHANNEL_DEFAULT.toMutableStateList()
    }
}

@Composable
fun PreferenceItem(
    preference: Preference.PreferenceItem<*>,
) {
    val prefs by Prefs.dsData.collectAsState(initial = null)
    val context = LocalContext.current
    when (preference) {
        is Preference.PreferenceItem.TextPreference -> {
            TextPreferenceWidget(
                preference = preference,
                onClick = { preference.onClick(context) }
            )
        }
        is Preference.PreferenceItem.SwitchPreference -> {
            SwitchPreferenceWidget(
                preference = preference,
                value = Prefs.dataStore.get(
                    preference.key,
                    false
                ).value.flow.collectAsState(initial = false).value,
                onValueChange = { newValue ->
                    Prefs.set(preference.key, newValue)
                    preference.onClick(context, newValue)
                }
            )
        }
        is Preference.PreferenceItem.ListPreference -> {
            ListPreferenceWidget(
                preference = preference,
                value = prefs?.get(stringPreferencesKey(preference.key)) ?: run {
                    Prefs.get<String>(preference.key) ?: run {
                        Prefs.set(preference.key, preference.default)
                    }
                    preference.default
                },
                onValueChange = { newValue ->
                    Prefs.set(preference.key, newValue)
                }
            )
        }
        is Preference.PreferenceItem.EditPreference -> {
            EditPreferenceWidget(
                preference = preference,
                value = prefs?.get(stringPreferencesKey(preference.key)) ?: run {
                    Prefs.get<String>(preference.key) ?: run {
                        Prefs.set(preference.key, preference.default)
                    }
                    preference.default
                },
                default = preference.default,
                summary = { v, m -> if (m) "共${v.splitByOr().size}条数据" else "${if (v.isBlank()) "" else "当前："}$v" },
                onValueChange = { newValue ->
                    Prefs.set(preference.key, newValue)
                }
            )
        }
        is Preference.PreferenceItem.CheckboxListPreference -> {
            val items by lazy {
                Prefs.get<String>(preference.key)?.fromJsonList<CheckBoxItem>()
                    ?.toMutableStateList() ?: run {
                    Prefs.set(preference.key, preference.items.toJson())
                    preference.items
                }
            }
            CheckBoxListPreferenceWidget(
                preference = preference,
                items = items,
                onDismiss = {
                    Prefs.set(preference.key, items.toJson())
                }
            )
        }
        is Preference.PreferenceItem.ChannelListPreference -> {
            ChannelListPreferenceWidget(
                preference = preference,
                value = prefs?.get(booleanPreferencesKey(preference.key)) ?: false,
                onValueChange = { newValue ->
                    Prefs.set(preference.key, newValue)
                },
                items = channelItems,
                onDismiss = {
                    Prefs.set(CHANNEL_KEY, channelItems.toJson())
                }
            )
        }
    }
}

@Composable
fun PreferenceScreen(
    index: Int,
    state: LazyListState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(),
        state = state
    ) {
        prefItems[index].map { item ->
            when (item) {
                is TextItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.TextPreference(
                            title = item.title,
                            summary = item.summary,
                            onClick = item.onClick
                        )
                    )
                }
                is SwitchItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.SwitchPreference(
                            key = item.key,
                            title = item.title,
                            summary = item.summary,
                            dependency = item.dependency,
                            onClick = item.onClick
                        )
                    )
                }
                is EditItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.EditPreference(
                            key = item.key,
                            title = item.title,
                            default = item.default,
                            multi = item.multi,
                            dependency = item.dependency
                        )
                    )
                }
                is ListItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.ListPreference(
                            key = item.key,
                            title = item.title,
                            default = item.default,
                            entries = item.entries,
                            dependency = item.dependency
                        )
                    )
                }
                is CheckBoxListItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.CheckboxListPreference(
                            key = item.key,
                            title = item.title,
                            summary = item.summary,
                            items = item.items,
                            dependency = item.dependency
                        )
                    )
                }
                is ChannelListItem -> item {
                    PreferenceItem(
                        Preference.PreferenceItem.ChannelListPreference(
                            key = item.key,
                            title = item.title,
                            dialogTitle = item.dialogTitle,
                            summary = item.summary,
                            dependency = item.dependency
                        )
                    )
                }
                ItemDivider -> item {
                    Divider()
                }
                null -> {}
            }
        }
    }
}
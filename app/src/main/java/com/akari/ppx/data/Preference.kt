package com.akari.ppx.data

import android.content.Context
import androidx.compose.runtime.Composable
import com.akari.ppx.data.model.CheckBoxItem

sealed class Preference {
    abstract val title: String
    abstract val enabled: Boolean

    sealed class PreferenceItem<T> : Preference() {
        abstract val summary: String?
        abstract val singleLineTitle: Boolean
        abstract val icon: @Composable (() -> Unit)?
        abstract val dependency: String?

        data class TextPreference(
            val onClick: (Context) -> Unit = {},

            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<String>()

        data class EditPreference(
            val key: String,
            val default: String,
            val multi: Boolean,

            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<String>()

        data class SwitchPreference(
            val key: String,
            val onClick: (Context, Boolean) -> Unit = { _, _ -> },

            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<Boolean>()

        data class ListPreference(
            val key: String,
            val entries: Map<String, String>,
            val default: String,

            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<String>()

        data class CheckboxListPreference(
            val key: String,
            val items: List<CheckBoxItem>,

            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<String>()

        data class ChannelListPreference(
            val key: String,

            override val title: String,
            val dialogTitle: String = title,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = false,
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val dependency: String? = null
        ) : PreferenceItem<String>()
    }
}
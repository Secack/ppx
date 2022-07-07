package com.akari.ppx.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.akari.ppx.data.Preference
import com.akari.ppx.data.Prefs
import com.akari.ppx.utils.rememberState

@Composable
fun EditPreferenceWidget(
    preference: Preference.PreferenceItem.EditPreference,
    value: String,
    default: String,
    summary: (String, Boolean) -> String = { s, _ -> s },
    onValueChange: (String) -> Unit
) {
    var isDialogShown by rememberState(value = false)
    val changeDialogShown = { isDialogShown = !isDialogShown }
    TextPreferenceWidget(
        preference = preference,
        summary = summary(value, preference.multi),
        onClick = {
            preference.dependency?.let { Prefs.set(it, true) }
            changeDialogShown()
        }
    )
    if (isDialogShown) {
        var text by rememberState(value = value)
        val submit = {
            onValueChange(text)
            changeDialogShown()
        }
        AlertDialog(
            onDismissRequest = { changeDialogShown() },
            title = {
                Text(
                    text = preference.title,
                    color = MaterialTheme.colors.primary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        submit()
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onValueChange(default)
                        changeDialogShown()
                    }
                ) {
                    Text("重置")
                }
            },
            text = {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "输入${preference.title}",
                                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                    cursorBrush = SolidColor(MaterialTheme.colors.primary)
                )
            },
        )
    }
}

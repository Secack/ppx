package com.akari.ppx.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akari.ppx.BuildConfig.VERSION_NAME
import com.akari.ppx.R
import com.akari.ppx.data.model.VersionWrapper
import com.akari.ppx.ui.theme.LIGHT_PINK
import com.akari.ppx.utils.rememberState
import com.akari.ppx.utils.showToast

@Composable
fun StatusCardWidget(
    isActive: Boolean,
    updates: MutableState<VersionWrapper>,
    targetVersion: String,
    onClick: () -> Unit = {}
) {
    data class CardData(
        val title: String = "已激活",
        val summary: String = "当前版本：$VERSION_NAME 皮皮虾版本：$targetVersion",
        val bgColor: Color = LIGHT_PINK,
        val imageVector: ImageVector? = null,
        val painter: Painter? = null,
    )

    val context = LocalContext.current
    var isDialogShown by rememberState(value = false)
    val changeDialogShown = { isDialogShown = !isDialogShown }
    val current = updates.value.current
    val latest = updates.value.latest
    val isLatest = current?.version == latest?.version
    val cardData = when (isActive) {
        /* 已激活 */ true -> when (current?.hasMatch(targetVersion)) {
            /* 已适配 */ true -> when (isLatest) {
                /* 无更新 */ true -> CardData(
                    imageVector = Icons.Filled.CheckCircle
                )
                /* 有更新 */ false -> CardData(
                    title = "已激活 (发现更新)",
                    imageVector = Icons.Filled.CheckCircle
                )
            }
            /* 未适配 */ false -> when (isLatest) {
                /* 无更新 */ true -> CardData(
                    summary = "当前版本($VERSION_NAME)不适配皮皮虾$targetVersion",
                    bgColor = Color.Red,
                    imageVector = Icons.Filled.Warning
                )
                /* 有更新 */ false -> CardData(
                    title = "已激活 (发现更新)",
                    summary = "当前版本($VERSION_NAME)不适配皮皮虾$targetVersion",
                    bgColor = Color.Red,
                    imageVector = Icons.Filled.Warning
                )

            }
            else -> null
        } ?: CardData(
            imageVector = Icons.Filled.CheckCircle
        )
        /* 未激活 */ false -> CardData(
            title = "未激活",
            summary = "模块功能将不会生效",
            bgColor = Color.Black,
            painter = painterResource(R.drawable.ic_error)
        )
    }
    cardData.run {
        Spacer(modifier = Modifier.height(15.dp))
        Card(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.12f)
                .clickable {
                    changeDialogShown()
                },
            backgroundColor = bgColor,
            shape = RoundedCornerShape(12.dp),
            elevation = 6.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(25.dp))
                Box {
                    imageVector?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    }
                    painter?.let {
                        Icon(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    }
                }
                ListItem(
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            color = Color.White
                        )
                    },
                    secondaryText = {
                        Text(
                            text = summary,
                            color = Color.White
                        )
                    })
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
    if (isDialogShown) {
        latest?.msg?.let {
            context.showToast("当前适配版本：${current?.matchesVersion}")
            AlertDialog(
                onDismissRequest = { changeDialogShown() },
                title = {
                    Text(
                        text = "信息 ${latest.version}",
                        color = MaterialTheme.colors.primary
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            changeDialogShown()
                            if (!isLatest)
                                onClick()
                        }
                    ) {
                        Text(if (isLatest) "确定" else "获取更新")
                    }
                },
                dismissButton = {
                    if (!isLatest) {
                        TextButton(
                            onClick = {
                                changeDialogShown()
                            }
                        ) {
                            Text("取消")
                        }
                    }
                },
                text = {
                    SelectionContainer {
                        Text(text = it)
                    }
                },
            )
        } ?: run {
            context.showToast("获取中，请稍候")
        }
    }
}
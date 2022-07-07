package com.akari.ppx.ui.screen

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akari.ppx.BuildConfig.VERSION_NAME
import com.akari.ppx.R
import com.akari.ppx.ui.theme.ORANGE
import com.akari.ppx.ui.widget.AboutCardWidget
import com.akari.ppx.utils.*

@Composable
fun AboutScreen(isActive: Boolean) {
    val context = LocalContext.current
    var isRotated by rememberState(value = false)
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) 360F else 0F,
        animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing)
    )
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .rotate(rotationAngle)
                .clickable {
                    isRotated = !isRotated
                },
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "某科学の阿卡林 个人开发",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Copyright © 2020-2022 Secack",
            fontSize = 10.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        AboutCardWidget(
            modifier = Modifier.fillMaxHeight(0.1f),
            backgroundColor = Color(246, 233, 238),
            onClick = {
                context.showToast("感谢支持! (ㅅ´ ˘ `)♡~")
                startAlipay(context)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_verified),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = ORANGE
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "赞赏",
                color = ORANGE,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        AboutCardWidget(
            modifier = Modifier.fillMaxHeight(0.11f),
            onClick = { openGitPage(context) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_github),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "项目源码",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        AboutCardWidget(
            modifier = Modifier.fillMaxHeight(0.13f),
            onClick = { joinTelegram(context) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_telegram),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "订阅频道",
                fontWeight = FontWeight.Bold
            )
        }
        isActive.check(true) {
            Spacer(modifier = Modifier.height(15.dp))
            AboutCardWidget(
                modifier = Modifier.fillMaxHeight(0.16f),
                onClick = { joinQQGroup(context) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qq_group),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "加入Q群",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "特别鸣谢：酷安@污帝多么寂寞",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "版本号: $VERSION_NAME",
            fontSize = 10.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}
package com.akari.ppx.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akari.ppx.ui.screen.PreferenceScreen
import com.akari.ppx.ui.theme.BaseTheme
import com.akari.ppx.ui.theme.GREY
import com.akari.ppx.ui.widget.StatusCardWidget
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.akari.ppx.BuildConfig.APPLICATION_ID
import com.akari.ppx.R
import com.akari.ppx.data.model.VersionWrapper
import com.akari.ppx.data.prefTabs
import com.akari.ppx.ui.screen.AboutScreen
import com.akari.ppx.utils.VersionChecker
import com.akari.ppx.utils.VersionChecker.getUpdates
import com.akari.ppx.utils.VersionChecker.targetVersion
import com.akari.ppx.utils.openBrowser
import com.akari.ppx.utils.rememberState
import com.akari.ppx.utils.startPPX

class MainActivity : ComponentActivity() {
    private lateinit var scaffoldState: ScaffoldState
    private lateinit var scope: CoroutineScope
    private lateinit var fabVisible: MutableState<Boolean>
    private var lastScrollOffset = 0
    private var currentScrollOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isActive = isModuleActive(this@MainActivity)
        setContent {
            BaseTheme {
                scaffoldState = rememberScaffoldState()
                scope = rememberCoroutineScope()
                fabVisible = rememberState(value = true)
                var currentIndex by rememberState(value = 0)
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(modifier = Modifier.height(40.dp), elevation = 0.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = null,
                                )
                                Text(
                                    text = stringResource(R.string.app_name),
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = fabVisible.value,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it }
                        ) {
                            FloatingActionButton(
                                onClick = { startPPX(this@MainActivity) },
                                modifier = Modifier.padding(
                                    horizontal = 55.dp,
                                    vertical = 35.dp
                                )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ppx_logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(40.dp)
                                )
                            }
                        }
                    }
                ) {
                    val state = rememberPagerState()
                    currentIndex = state.currentPage
                    Column(Modifier.fillMaxSize()) {
                        TabRow(
                            selectedTabIndex = currentIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(40.dp),
                            indicator = { tabPositions ->
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(tabPositions[currentIndex])
                                        .height(4.dp)
                                        .padding(horizontal = 20.dp)
                                        .background(
                                            color = Color.White,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                )
                            },
                            divider = {},
                            backgroundColor = MaterialTheme.colors.primary
                        ) {
                            prefTabs.forEachIndexed { index, _ ->
                                Tab(
                                    selected = index == currentIndex,
                                    onClick = {
                                        currentIndex = index
                                        scope.launch {
                                            state.scrollToPage(index)
                                        }
                                    },
                                    text = {
                                        Text(prefTabs[index], maxLines = 1)
                                    },
                                    selectedContentColor = Color.White,
                                    unselectedContentColor = GREY
                                )
                            }
                        }
                        HorizontalPager(
                            count = prefTabs.size,
                            state = state
                        ) { index ->
                            val listState = rememberLazyListState()
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when (index) {
                                    /* 主页 */ 0 -> {
                                    val updates =
                                        rememberState(VersionWrapper()).also {
                                            scope.launch {
                                                it.value = getUpdates()
                                            }
                                        }
                                    StatusCardWidget(
                                        isActive = isActive,
                                        updates = updates,
                                        targetVersion = targetVersion
                                    ) {
                                        updates.value.latest?.url?.let { openBrowser(it) }
                                    }
                                }
                                    /* 关于 */ 4 -> {
                                    AboutScreen(isActive)
                                }
                                }
                                if (!listState.isScrollInProgress) {
                                    lastScrollOffset = currentScrollOffset
                                    currentScrollOffset =
                                        listState.firstVisibleItemScrollOffset + listState.firstVisibleItemIndex * 1000
                                    fabVisible.value =
                                        lastScrollOffset - currentScrollOffset >= 0 && currentIndex != 4
                                }
                                PreferenceScreen(index, listState)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun isModuleActive(context: Context) = runCatching {
            val uri = Uri.parse("content://me.weishu.exposed.CP/")
            context.contentResolver.call(uri, "active", null, null)?.getBoolean("active", false)
                ?: false
        }.getOrDefault(false)

        operator fun invoke(context: Context) = Intent().also {
            ComponentName(APPLICATION_ID, this::class.java.name.split("$")[0]).let(it::setComponent)
        }.let { context.startActivity(it) }
    }
}

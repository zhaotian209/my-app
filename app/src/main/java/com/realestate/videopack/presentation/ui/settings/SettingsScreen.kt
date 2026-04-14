package com.realestate.videopack.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.realestate.videopack.presentation.theme.Black12
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.Surface
import com.realestate.videopack.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "设置",
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Black12)
        ) {
            item {
                // 通用设置
                SettingCard("通用设置") {
                    SettingItem("视频质量", "高清 (1080p)")
                    SettingItem("保存位置", "内部存储 / 视频")
                }
            }

            item {
                // 配音设置
                SettingCard("配音设置") {
                    SettingItem("默认配音", "男声")
                    SettingItem("音量", "80%")
                }
            }

            item {
                // 画质设置
                SettingCard("画质设置") {
                    SettingItem("画质增强", "开启")
                    SettingItem("视频滤镜", "默认")
                }
            }

            item {
                // 存储设置
                SettingCard("存储设置") {
                    SettingItem("缓存大小", "100MB")
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "清理缓存",
                            color = Black12,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item {
                // 关于
                SettingCard("关于") {
                    SettingItem("版本", "1.0.0")
                    SettingItem("版权", "© 2026 房产视频包装")
                }
            }
        }
    }
}

@Composable
fun SettingCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Gold, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                title,
                color = Gold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            label,
            color = White,
            fontSize = 14.sp
        )
        Text(
            value,
            color = Gold,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

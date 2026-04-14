package com.realestate.videopack.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.GoldDark
import com.realestate.videopack.presentation.theme.Surface
import com.realestate.videopack.presentation.theme.White

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "设置",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
        )

        // 设置选项卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = GoldDark,
                    ambientColor = GoldDark
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Surface.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "视频质量",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
                Text(
                    text = "高清 (1080p)",
                    fontSize = 14.sp,
                    color = Gold
                )
            }
        }

        // 其他设置选项卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = GoldDark,
                    ambientColor = GoldDark
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Surface.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "保存位置",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
                Text(
                    text = "内部存储 / 视频",
                    fontSize = 14.sp,
                    color = Gold
                )
            }
        }

        // 关于卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = GoldDark,
                    ambientColor = GoldDark
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Surface.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "关于",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
                Text(
                    text = "房产视频一键包装 v1.0",
                    fontSize = 14.sp,
                    color = Gold
                )
                Text(
                    text = "© 2026 房产视频包装",
                    fontSize = 12.sp,
                    color = Gold
                )
            }
        }
    }
}

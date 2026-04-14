package com.realestate.videopack.presentation.videolibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realestate.videopack.presentation.theme.Black12
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.GoldDark
import com.realestate.videopack.presentation.theme.Surface
import com.realestate.videopack.presentation.theme.White

@Composable
fun VideoLibraryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "视频库",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Gold,
            modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
        )

        // 视频列表
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(videoList) {
                VideoCard(video = it)
            }
        }
    }
}

@Composable
fun VideoCard(video: VideoItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
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
            modifier = Modifier.fillMaxSize()
        ) {
            // 视频缩略图
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GoldDark, Gold)
                        ),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "视频预览",
                    color = Black12,
                    fontWeight = FontWeight.Medium
                )
            }

            // 视频信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = video.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = White,
                    maxLines = 1
                )
                Text(
                    text = video.duration,
                    fontSize = 12.sp,
                    color = Gold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// 示例视频数据
val videoList = listOf(
    VideoItem("房产展示视频1", "01:30"),
    VideoItem("房产展示视频2", "02:15"),
    VideoItem("房产展示视频3", "01:45"),
    VideoItem("房产展示视频4", "02:30"),
    VideoItem("房产展示视频5", "01:20"),
    VideoItem("房产展示视频6", "02:00")
)

data class VideoItem(val title: String, val duration: String)

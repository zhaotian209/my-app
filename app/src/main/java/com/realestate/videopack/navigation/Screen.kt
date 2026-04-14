package com.realestate.videopack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : Screen("home", "首页", Icons.Default.Home)
    object VideoLibrary : Screen("video_library", "视频库", Icons.Default.List)
    object Batch : Screen("batch", "批量", Icons.Default.List)
    object Settings : Screen("settings", "我的", Icons.Default.Settings)
    object Upload : Screen("upload", "上传视频")
    object HouseForm : Screen("house_form", "房产信息")
    object Preview : Screen("preview", "预览")
}

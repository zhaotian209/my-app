package com.realestate.videopack.presentation.ui.upload

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.realestate.videopack.presentation.theme.Black12
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.Surface
import com.realestate.videopack.presentation.theme.White
import com.realestate.videopack.presentation.ui.upload.UploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavHostController, viewModel: UploadViewModel) {
    val context = LocalContext.current
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCoverUri by remember { mutableStateOf<Uri?>(null) }
    var selectedAspectRatio by remember { mutableStateOf("16:9") }

    // 选择视频
    fun selectVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        context.startActivity(Intent.createChooser(intent, "选择视频"))
    }

    // 选择封面图片
    fun selectCover() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        context.startActivity(Intent.createChooser(intent, "选择封面图片"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "上传视频",
                        color = Gold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回",
                        modifier = Modifier.padding(16.dp),
                        tint = Gold
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Black12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // 视频选择卡片
            Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .border(1.dp, Gold, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    onClick = ::selectVideo
                ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.VideoLibrary,
                        contentDescription = "选择视频",
                        modifier = Modifier.size(64.dp),
                        tint = Gold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (selectedVideoUri != null) "已选择视频" else "点击选择视频",
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 封面选择卡片
            Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp)
                        .border(1.dp, Gold, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    onClick = ::selectCover
                ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "选择封面",
                        modifier = Modifier.size(48.dp),
                        tint = Gold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        if (selectedCoverUri != null) "已选择封面" else "点击选择封面",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 比例选择
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "选择视频比例",
                    color = Gold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AspectRatioButton("16:9", selected = selectedAspectRatio == "16:9") { selectedAspectRatio = "16:9" }
                    AspectRatioButton("9:16", selected = selectedAspectRatio == "9:16") { selectedAspectRatio = "9:16" }
                    AspectRatioButton("1:1", selected = selectedAspectRatio == "1:1") { selectedAspectRatio = "1:1" }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            // 下一步按钮
            Button(
                onClick = {
                    if (selectedVideoUri != null && selectedCoverUri != null) {
                        // 保存选择的视频和封面
                        navController.navigate("house_form")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedVideoUri != null && selectedCoverUri != null
            ) {
                Text(
                    "下一步",
                    color = Black12,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AspectRatioButton(text: String, selected: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp, 48.dp)
            .then(if (!selected) Modifier.border(1.dp, Gold, RoundedCornerShape(12.dp)) else Modifier),
        colors = if (selected) {
            ButtonDefaults.buttonColors(
                containerColor = Gold
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = Surface
            )
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text,
            color = if (selected) Black12 else Gold,
            fontWeight = FontWeight.Medium
        )
    }
}

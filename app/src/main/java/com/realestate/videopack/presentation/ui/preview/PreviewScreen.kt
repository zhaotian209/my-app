package com.realestate.videopack.presentation.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
fun PreviewScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "视频预览",
                        color = Gold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Black12)
        ) {
            VideoPreviewPlayer()
            EditPanel()
            ExportButton()
        }
    }
}

@Composable
fun VideoPreviewPlayer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .padding(16.dp)
            .border(1.dp, Gold, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.List,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp),
                tint = Gold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPanel() {
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
                "编辑文案",
                color = Gold,
                fontWeight = FontWeight.Bold
            )
            val text = remember { mutableStateOf("") }
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Surface)
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Gold
                ),
                textStyle = androidx.compose.ui.text.TextStyle(color = White)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SettingToggle("字幕")
                SettingToggle("BGM")
                SettingToggle("画质增强")
            }
        }
    }
}

@Composable
fun SettingToggle(text: String) {
    val checked = remember { mutableStateOf(true) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            color = White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            colors = SwitchDefaults.colors(
                checkedTrackColor = Gold,
                uncheckedTrackColor = Surface,
                checkedThumbColor = White,
                uncheckedThumbColor = Color.Gray
            )
        )
    }
}

@Composable
fun ExportButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gold
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            "一键导出",
            color = Black12,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

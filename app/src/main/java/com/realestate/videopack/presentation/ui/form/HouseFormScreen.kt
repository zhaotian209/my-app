package com.realestate.videopack.presentation.ui.form

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.realestate.videopack.core.engine.AutoVideoProcessor
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.presentation.theme.Black12
import com.realestate.videopack.presentation.theme.Gold
import com.realestate.videopack.presentation.theme.Surface
import com.realestate.videopack.presentation.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseFormScreen(navController: NavHostController, viewModel: HouseFormViewModel) {
    val context = LocalContext.current
    var community by remember { mutableStateOf("") }
    var layout by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var decoration by remember { mutableStateOf("") }
    var sellingPoints by remember { mutableStateOf("") }
    var selectedTemplate by remember { mutableStateOf("现代简约") }
    var selectedVoice by remember { mutableStateOf("男声") }
    var isProcessing by remember { mutableStateOf(false) }
    
    // 从 ViewModel 获取 AutoVideoProcessor
    val autoVideoProcessor = viewModel.autoVideoProcessor

    // 保存并生成视频
    fun saveAndGenerateVideo() {
        if (community.isBlank() || layout.isBlank() || area.isBlank() || price.isBlank()) {
            // 显示错误提示
            return
        }

        isProcessing = true

        // 创建房屋信息对象
        val houseInfo = HouseInfo(
            community = community,
            layout = layout,
            area = area,
            price = price,
            decoration = decoration,
            sellingPoints = sellingPoints.split(",").map { it.trim() }
        )

        // 模拟视频和封面路径
        val videoPath = "" // 实际项目中应该从 UploadViewModel 获取
        val coverPath = "" // 实际项目中应该从 UploadViewModel 获取

        // 处理视频
        CoroutineScope(Dispatchers.IO).launch {
            val result = autoVideoProcessor.processVideo(videoPath, coverPath, houseInfo)
            isProcessing = false

            if (result.isSuccess) {
                val outputPath = result.getOrThrow()
                // 导航到预览页面
                navController.navigate("preview")
            } else {
                val error = result.exceptionOrNull()
                // 显示错误提示
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "房产信息",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Black12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                // 基本信息卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, Gold, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            "基本信息",
                            color = Gold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // 小区名称
                        FormTextField("小区名称", "请输入小区名称") { community = it }
                        
                        // 户型
                        FormTextField("户型", "请输入户型，如：3室2厅1卫") { layout = it }
                        
                        // 面积
                        FormTextField("面积", "请输入面积，如：120平米") { area = it }
                        
                        // 价格
                        FormTextField("价格", "请输入价格，如：300万") { price = it }
                        
                        // 装修
                        FormTextField("装修", "请输入装修情况，如：精装修") { decoration = it }
                        
                        // 卖点
                        FormTextField("卖点", "请输入房产卖点，多个卖点用逗号分隔") { sellingPoints = it }
                    }
                }
            }
            
            item {
                // 模板选择卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, Gold, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            "模板选择",
                            color = Gold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TemplateButton("现代简约", selected = selectedTemplate == "现代简约") { selectedTemplate = "现代简约" }
                            TemplateButton("豪华欧式", selected = selectedTemplate == "豪华欧式") { selectedTemplate = "豪华欧式" }
                            TemplateButton("中式风格", selected = selectedTemplate == "中式风格") { selectedTemplate = "中式风格" }
                        }
                    }
                }
            }
            
            item {
                // 配音选择卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, Gold, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            "配音选择",
                            color = Gold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            VoiceButton("男声", selected = selectedVoice == "男声") { selectedVoice = "男声" }
                            VoiceButton("女声", selected = selectedVoice == "女声") { selectedVoice = "女声" }
                            VoiceButton("儿童声", selected = selectedVoice == "儿童声") { selectedVoice = "儿童声" }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            item {
                // 保存按钮
                Button(
                    onClick = ::saveAndGenerateVideo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isProcessing
                ) {
                    Text(
                        if (isProcessing) "处理中..." else "保存并生成视频",
                        color = Black12,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun FormTextField(label: String, placeholder: String, onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = { Text(label, color = Gold) },
        placeholder = { Text(placeholder, color = White.copy(alpha = 0.6f)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = Gold.copy(alpha = 0.6f),
            focusedLabelColor = Gold,
            unfocusedLabelColor = Gold.copy(alpha = 0.6f),
            cursorColor = Gold
        )
    )
}

@Composable
fun TemplateButton(text: String, selected: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(100.dp)
            .height(48.dp)
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VoiceButton(text: String, selected: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(80.dp)
            .height(48.dp)
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

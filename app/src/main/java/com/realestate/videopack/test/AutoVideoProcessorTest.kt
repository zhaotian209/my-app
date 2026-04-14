package com.realestate.videopack.test

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.realestate.videopack.core.engine.AutoVideoProcessor
import com.realestate.videopack.data.local.entity.HouseInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AutoVideoProcessorTest : AppCompatActivity() {
    
    @Inject
    lateinit var autoVideoProcessor: AutoVideoProcessor
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 测试数据
        val videoPath = "${filesDir}/test_video.mp4"
        val coverImagePath = "${filesDir}/test_cover.jpg"
        val houseInfo = HouseInfo(
            community = "阳光小区",
            layout = "3室2厅",
            area = "120",
            price = "88万",
            decoration = "精装修",
            sellingPoints = listOf("南北通透", "采光好", "交通便利"),
            floor = "10楼",
            orientation = "南北",
            elevator = true,
            metro = "地铁2号线"
        )
        
        // 运行测试
        CoroutineScope(Dispatchers.IO).launch {
            val result = autoVideoProcessor.processVideo(videoPath, coverImagePath, houseInfo)
            
            if (result.isSuccess) {
                val outputPath = result.getOrThrow()
                println("测试成功！生成的视频路径：$outputPath")
            } else {
                val error = result.exceptionOrNull()
                println("测试失败！错误信息：${error?.message}")
            }
        }
    }
}
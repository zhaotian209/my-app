package com.realestate.videopack.core.engine

import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.model.VideoMeta
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AiScriptGeneratorTest {
    
    private lateinit var aiScriptGenerator: AiScriptGenerator
    
    @Before
    fun setUp() {
        // 创建 AiScriptGenerator 实例
        aiScriptGenerator = AiScriptGenerator()
    }
    
    @Test
    fun testGenerateScript() = runBlocking {
        // 测试数据
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
        
        val videoMeta = VideoMeta(
            path = "/tmp/video.mp4",
            durationMs = 15000, // 15秒
            width = 1920,
            height = 1080,
            fps = 30.0f
        )
        
        // 执行测试
        val result = aiScriptGenerator.generateScript(houseInfo, videoMeta)
        
        // 验证结果
        Assert.assertTrue(result.isSuccess)
        val script = result.getOrThrow()
        Assert.assertNotNull(script)
        Assert.assertTrue(script.isNotEmpty())
        // 验证脚本包含房屋信息
        Assert.assertTrue(script.contains("阳光小区"))
        Assert.assertTrue(script.contains("3室2厅"))
        Assert.assertTrue(script.contains("120"))
        Assert.assertTrue(script.contains("88万"))
    }
    
    @Test
    fun testCalculateTargetWordCount() {
        // 测试计算目标词数
        val durationMs = 15000 // 15秒
        val expectedWordCount = (15 * 135 / 60).toInt()
        
        // 反射获取私有方法
        val method = AiScriptGenerator::class.java.getDeclaredMethod("calculateTargetWordCount", Long::class.java)
        method.isAccessible = true
        val result = method.invoke(aiScriptGenerator, durationMs) as Int
        
        // 验证结果
        Assert.assertEquals(expectedWordCount, result)
    }
    
    @Test
    fun testCalculateScriptDuration() {
        // 测试计算脚本时长
        val script = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
        
        val duration = aiScriptGenerator.calculateScriptDuration(script)
        
        // 验证结果
        Assert.assertTrue(duration > 0)
    }
    
    @Test
    fun testAnalyzeVideoScene() {
        // 测试视频场景分析
        val videoMeta = VideoMeta(
            path = "/tmp/commercial_video.mp4",
            durationMs = 65000, // 65秒
            width = 1920,
            height = 1080,
            fps = 30.0f
        )
        
        val sceneType = aiScriptGenerator.analyzeVideoScene(videoMeta)
        
        // 验证结果
        Assert.assertNotNull(sceneType)
    }
    
    @Test
    fun testFineTuneScript() {
        // 测试脚本微调
        val script = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
        val targetDurationMs = 10000 // 10秒
        
        val fineTunedScript = aiScriptGenerator.fineTuneScript(
            script,
            targetDurationMs,
            AiScriptGenerator.AdjustmentDirection.SHORTEN
        )
        
        // 验证结果
        Assert.assertNotNull(fineTunedScript)
        Assert.assertTrue(fineTunedScript.isNotEmpty())
    }
}

package com.realestate.videopack.core.engine

import com.realestate.videopack.data.local.entity.HouseInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class AutoVideoProcessorTest {
    
    private lateinit var autoVideoProcessor: AutoVideoProcessor
    private lateinit var aiScriptGenerator: AiScriptGenerator
    private lateinit var ttsManager: TtsManager
    private lateinit var videoProcessingEngine: VideoProcessingEngine
    private lateinit var complianceChecker: ComplianceChecker
    private lateinit var coverGenerator: CoverGenerator
    
    @Before
    fun setUp() {
        // 模拟依赖
        aiScriptGenerator = Mockito.mock(AiScriptGenerator::class.java)
        ttsManager = Mockito.mock(TtsManager::class.java)
        videoProcessingEngine = Mockito.mock(VideoProcessingEngine::class.java)
        complianceChecker = Mockito.mock(ComplianceChecker::class.java)
        coverGenerator = Mockito.mock(CoverGenerator::class.java)
        
        // 创建 AutoVideoProcessor 实例
        autoVideoProcessor = AutoVideoProcessor(
            aiScriptGenerator = aiScriptGenerator,
            ttsManager = ttsManager,
            videoProcessingEngine = videoProcessingEngine,
            complianceChecker = complianceChecker,
            coverGenerator = coverGenerator
        )
        
        // 模拟方法返回值
        `when`(coverGenerator.generateCover(Mockito.anyString(), Mockito.any(HouseInfo::class.java)))
            .thenReturn("/tmp/cover.jpg")
        
        `when`(aiScriptGenerator.generateScript(Mockito.any(HouseInfo::class.java), Mockito.any()))
            .thenReturn(Result.success("阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"))
        
        `when`(ttsManager.synthesizeText(Mockito.anyString()))
            .thenReturn("/tmp/audio.mp3")
    }
    
    @Test
    fun testProcessVideo() = runBlocking {
        // 测试数据
        val videoPath = "/tmp/video.mp4"
        val coverImagePath = "/tmp/cover_image.jpg"
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
        
        // 执行测试
        val result = autoVideoProcessor.processVideo(videoPath, coverImagePath, houseInfo)
        
        // 验证结果
        Assert.assertTrue(result.isSuccess)
        val outputPath = result.getOrThrow()
        Assert.assertNotNull(outputPath)
        Assert.assertTrue(outputPath.endsWith(".mp4"))
    }
    
    @Test
    fun testProcessVideoWithAiFailure() = runBlocking {
        // 模拟 AI 生成失败
        `when`(aiScriptGenerator.generateScript(Mockito.any(HouseInfo::class.java), Mockito.any()))
            .thenReturn(Result.failure(Exception("AI generation failed")))
        
        // 测试数据
        val videoPath = "/tmp/video.mp4"
        val coverImagePath = "/tmp/cover_image.jpg"
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
        
        // 执行测试
        val result = autoVideoProcessor.processVideo(videoPath, coverImagePath, houseInfo)
        
        // 验证结果
        Assert.assertTrue(result.isSuccess)
        val outputPath = result.getOrThrow()
        Assert.assertNotNull(outputPath)
        Assert.assertTrue(outputPath.endsWith(".mp4"))
    }
}
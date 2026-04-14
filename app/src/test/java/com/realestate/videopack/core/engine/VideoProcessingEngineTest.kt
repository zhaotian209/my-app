package com.realestate.videopack.core.engine

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VideoProcessingEngineTest {
    
    private lateinit var videoProcessingEngine: VideoProcessingEngine
    
    @Before
    fun setUp() {
        // 创建 VideoProcessingEngine 实例
        videoProcessingEngine = VideoProcessingEngine()
    }
    
    @Test
    fun testBuildEnhanceFilter() {
        // 测试构建增强滤镜
        val brightness = 0.0f
        val contrast = 1.0f
        val saturation = 1.0f
        
        val filter = videoProcessingEngine.buildEnhanceFilter(brightness, contrast, saturation)
        
        // 验证结果
        Assert.assertNotNull(filter)
        Assert.assertTrue(filter.isNotEmpty())
        Assert.assertTrue(filter.contains("brightness=0.0"))
        Assert.assertTrue(filter.contains("contrast=1.0"))
        Assert.assertTrue(filter.contains("saturation=1.0"))
    }
    
    @Test
    fun testBuildAudioFilter() {
        // 测试构建音频滤镜
        val voiceTimeline = "0-10000"
        val totalDurationSec = 10.0f
        
        val filter = videoProcessingEngine.buildAudioFilter(voiceTimeline, totalDurationSec)
        
        // 验证结果
        Assert.assertNotNull(filter)
        Assert.assertTrue(filter.isNotEmpty())
        Assert.assertTrue(filter.contains("afade=t=out:st=8.0:d=2"))
    }
    
    @Test
    fun testBuildCoverExtractCommand() {
        // 测试构建封面提取命令
        val inputPath = "/tmp/video.mp4"
        val outputPath = "/tmp/cover.jpg"
        
        val command = videoProcessingEngine.buildCoverExtractCommand(inputPath, outputPath)
        
        // 验证结果
        Assert.assertNotNull(command)
        Assert.assertTrue(command.isNotEmpty())
        Assert.assertTrue(command.contains("-i /tmp/video.mp4"))
        Assert.assertTrue(command.contains("-ss 2"))
        Assert.assertTrue(command.contains("-t 1"))
        Assert.assertTrue(command.contains("/tmp/cover.jpg"))
    }
    
    @Test
    fun testBuildFullRenderCommand() {
        // 测试构建完整渲染命令
        val inputVideo = "/tmp/video.mp4"
        val inputAudio = "/tmp/audio.mp3"
        val inputBgm = "/tmp/bgm.mp3"
        val assSubtitle = "/tmp/subtitle.ass"
        val coverImage = "/tmp/cover.jpg"
        val outputPath = "/tmp/output.mp4"
        val enhanceFilter = "yadif=0:0:0"
        val audioFilter = "afftdn=nf=-25"
        
        val command = videoProcessingEngine.buildFullRenderCommand(
            inputVideo,
            inputAudio,
            inputBgm,
            assSubtitle,
            coverImage,
            outputPath,
            enhanceFilter,
            audioFilter
        )
        
        // 验证结果
        Assert.assertNotNull(command)
        Assert.assertTrue(command.isNotEmpty())
        Assert.assertTrue(command.contains("-i /tmp/cover.jpg"))
        Assert.assertTrue(command.contains("-i /tmp/video.mp4"))
        Assert.assertTrue(command.contains("-i /tmp/audio.mp3"))
        Assert.assertTrue(command.contains("-i /tmp/bgm.mp3"))
        Assert.assertTrue(command.contains("/tmp/output.mp4"))
    }
}

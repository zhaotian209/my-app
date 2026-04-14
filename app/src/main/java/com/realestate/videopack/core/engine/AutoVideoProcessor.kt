package com.realestate.videopack.core.engine

import android.media.MediaMetadataRetriever
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.model.VideoMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoVideoProcessor @Inject constructor(
    private val aiScriptGenerator: AiScriptGenerator,
    private val ttsManager: TtsManager,
    private val videoProcessingEngine: VideoProcessingEngine,
    private val complianceChecker: ComplianceChecker,
    private val coverGenerator: CoverGenerator
) {
    
    /**
     * 全自动处理视频
     * @param videoPath 原始视频路径
     * @param coverImagePath 用户上传的封面图片路径
     * @param houseInfo 房屋信息
     * @return 处理后的视频路径
     */
    suspend fun processVideo(
        videoPath: String,
        coverImagePath: String,
        houseInfo: HouseInfo
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. 分析视频信息
                val videoMeta = analyzeVideo(videoPath)
                
                // 2. 生成封面
                val coverPath = coverGenerator.generateCover(coverImagePath, houseInfo)
                
                // 3. 生成 AI 文案
                val script = generateScript(houseInfo, videoMeta)
                
                // 4. 生成配音
                val audioPath = generateAudio(script, videoMeta.durationMs)
                
                // 5. 生成字幕
                val subtitlePath = generateSubtitle(script, audioPath)
                
                // 6. 选择背景音乐
                val bgmPath = selectBackgroundMusic(videoMeta.durationMs)
                
                // 7. 合成视频
                val outputPath = combineVideo(
                    videoPath,
                    coverPath,
                    audioPath,
                    subtitlePath,
                    bgmPath
                )
                
                // 8. 清理临时文件
                cleanTempFiles(listOf(coverPath, audioPath, subtitlePath, bgmPath))
                
                Result.success(outputPath)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * 分析视频信息
     */
    private fun analyzeVideo(videoPath: String): VideoMeta {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoPath)
            
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationStr?.toLong() ?: 0
            
            val widthStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val width = widthStr?.toInt() ?: 1920
            
            val heightStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val height = heightStr?.toInt() ?: 1080
            
            val fpsStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)
            val fps = fpsStr?.toFloat() ?: 30.0f
            
            return VideoMeta(
                path = videoPath,
                durationMs = durationMs,
                width = width,
                height = height,
                fps = fps
            )
        } finally {
            retriever.release()
        }
    }
    
    /**
     * 生成 AI 文案
     */
    private suspend fun generateScript(houseInfo: HouseInfo, videoMeta: VideoMeta): String {
        // 计算目标词数（基于 135 词/分钟的语速）
        val targetWordCount = (videoMeta.durationMs / 1000.0 * 135 / 60).toInt()
        
        // 生成 AI 文案
        val scriptResult = aiScriptGenerator.generateScript(
            houseInfo = houseInfo,
            videoMeta = videoMeta
        )
        
        return if (scriptResult.isSuccess) {
            scriptResult.getOrThrow()
        } else {
            // 如果 AI 生成失败，使用传统方法生成脚本
            generateFallbackScript(houseInfo, targetWordCount)
        }
    }
    
    /**
     * 生成 fallback 脚本
     */
    private fun generateFallbackScript(houseInfo: HouseInfo, targetWordCount: Int): String {
        val script = StringBuilder()
        script.append("${houseInfo.community}，${houseInfo.layout}，${houseInfo.area}平米，")
        script.append("售价${houseInfo.price}，${houseInfo.decoration}装修。")
        
        if (houseInfo.sellingPoints.isNotEmpty()) {
            script.append("${houseInfo.sellingPoints.joinToString("，")}。")
        }
        
        // 根据目标词数调整脚本长度
        val currentWordCount = script.toString().split("\\s+").size
        if (currentWordCount < targetWordCount) {
            script.append("周围配套设施齐全，有学校、医院、商场等，生活非常方便。")
        } else if (currentWordCount > targetWordCount) {
            return script.toString().take(script.length * targetWordCount / currentWordCount)
        }
        
        return script.toString()
    }
    
    /**
     * 生成配音
     */
    private fun generateAudio(script: String, durationMs: Long): String {
        return ttsManager.synthesizeText(script)
    }
    
    /**
     * 生成字幕
     */
    private fun generateSubtitle(script: String, audioPath: String): String {
        // 这里简化处理，实际应该根据音频生成精确的字幕
        // 可以使用 TtsManager 生成带时间戳的字幕
        val subtitlePath = "${File(audioPath).parent}/subtitle.srt"
        val subtitleContent = generateSrtContent(script)
        File(subtitlePath).writeText(subtitleContent)
        return subtitlePath
    }
    
    /**
     * 生成 SRT 字幕内容
     */
    private fun generateSrtContent(script: String): String {
        val lines = script.split("，").filter { it.isNotEmpty() }
        val sb = StringBuilder()
        
        var startTime = 0
        val lineDuration = 3000 // 每行字幕显示 3 秒
        
        lines.forEachIndexed { index, line ->
            sb.append(index + 1).append("\n")
            sb.append(formatTime(startTime)).append(" --> ").append(formatTime(startTime + lineDuration)).append("\n")
            sb.append(line).append("\n\n")
            startTime += lineDuration
        }
        
        return sb.toString()
    }
    
    /**
     * 格式化时间为 SRT 格式
     */
    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000
        val ms = milliseconds % 1000
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d,%03d", minutes, secs, ms / 10, ms % 10)
    }
    
    /**
     * 选择背景音乐
     */
    private fun selectBackgroundMusic(durationMs: Long): String {
        // 这里简化处理，实际应该根据视频风格选择合适的背景音乐
        // 可以从内置的音乐库中选择，或者根据视频内容推荐
        val bgmPath = "${System.getProperty("java.io.tmpdir")}/bgm.mp3"
        // 实际项目中，这里应该复制内置的背景音乐到临时目录
        // 或者下载适合的背景音乐
        return bgmPath
    }
    
    /**
     * 合成视频
     */
    private fun combineVideo(
        videoPath: String,
        coverPath: String,
        audioPath: String,
        subtitlePath: String,
        bgmPath: String
    ): String {
        val outputPath = "${File(videoPath).parent}/output_${System.currentTimeMillis()}.mp4"
        
        // 使用 VideoProcessingEngine 合成视频
        // 实际项目中，这里应该调用具体的视频合成方法
        // 例如，使用 FFmpeg 命令合成视频
        
        // 简化实现：复制原始视频到输出路径
        File(videoPath).copyTo(File(outputPath), overwrite = true)
        
        return outputPath
    }
    
    /**
     * 清理临时文件
     */
    private fun cleanTempFiles(filePaths: List<String>) {
        filePaths.forEach { path ->
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}
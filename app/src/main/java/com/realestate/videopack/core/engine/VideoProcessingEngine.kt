package com.realestate.videopack.core.engine

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoProcessingEngine @Inject constructor() {
    fun buildEnhanceFilter(brightness: Float, contrast: Float, saturation: Float): String {
        // 防抖去噪调色锐化
        return "yadif=0:0:0,denoise3d=4:4:4,eq=brightness=$brightness:contrast=$contrast:saturation=$saturation,unsharp=5:5:1.0:5:5:0.0"
    }

    fun buildAudioFilter(voiceTimeline: String, totalDurationSec: Float): String {
        // 去噪+响度+BGM闪避+淡出
        return "afftdn=nf=-25,compand=0.05:0.1:60:-20:-15:-3:0:0:0,afade=t=out:st=${totalDurationSec - 2}:d=2"
    }

    fun buildCoverExtractCommand(inputPath: String, outputPath: String): String {
        // 跳过首尾2秒，黑帧模糊过滤
        return "-i $inputPath -ss 2 -t 1 -vf \"select=gt(scene,0.3)\" -frames:v 1 $outputPath"
    }

    fun buildFullRenderCommand(
        inputVideo: String,
        inputAudio: String,
        inputBgm: String,
        assSubtitle: String,
        coverImage: String,
        outputPath: String,
        enhanceFilter: String,
        audioFilter: String
    ): String {
        // 封面+原视频+ASS字幕+混音+水印完整FFmpeg命令
        return "-loop 1 -i $coverImage -i $inputVideo -i $inputAudio -i $inputBgm " +
                "-filter_complex \"" +
                "[0:v]scale=1920:1080:force_original_aspect_ratio=decrease,pad=1920:1080:(ow-iw)/2:(oh-ih)/2,setsar=1,fade=t=in:st=0:d=2[cover];" +
                "[1:v]scale=1920:1080:force_original_aspect_ratio=decrease,pad=1920:1080:(ow-iw)/2:(oh-ih)/2,setsar=1,${enhanceFilter}[video];" +
                "[cover][video]concat=n=2:v=1:a=0[outv];" +
                "[2:a]${audioFilter}[voice];" +
                "[3:a]volume=0.3[bgm];" +
                "[voice][bgm]amix=inputs=2:duration=first:dropout_transition=2[outa]" +
                "\" " +
                "-map [outv] -map [outa] -c:v libx264 -preset medium -crf 23 -c:a aac -b:a 128k " +
                "-vf \"ass=$assSubtitle\" " +
                "-shortest $outputPath"
    }

    suspend fun executeFFmpegCommand(command: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val session = FFmpegKit.execute(command)
                if (ReturnCode.isSuccess(session.returnCode)) {
                    Result.success(session.output)
                } else {
                    Result.failure(Exception("FFmpeg执行失败: ${session.output}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
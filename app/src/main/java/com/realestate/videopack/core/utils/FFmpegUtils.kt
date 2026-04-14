package com.realestate.videopack.core.utils

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

object FFmpegUtils {
    fun getVideoInfo(inputPath: String): Map<String, String> {
        val command = "-i $inputPath"
        val session = FFmpegKit.execute(command)
        val output = session.output
        val info = mutableMapOf<String, String>()

        // 解析输出信息
        output.lines().forEach { line ->
            when {
                line.contains("Duration:") -> {
                    val duration = line.split("Duration:")[1].split(",")[0].trim()
                    info["duration"] = duration
                }
                line.contains("Stream #0:0") && line.contains("Video") -> {
                    val resolution = line.split(",")[2].trim()
                    info["resolution"] = resolution
                }
                line.contains("Stream #0:1") && line.contains("Audio") -> {
                    val audioInfo = line.split(",")[2].trim()
                    info["audio"] = audioInfo
                }
            }
        }

        return info
    }

    fun isFFmpegAvailable(): Boolean {
        try {
            val session = FFmpegKit.execute("-version")
            return ReturnCode.isSuccess(session.returnCode)
        } catch (e: Exception) {
            return false
        }
    }
}
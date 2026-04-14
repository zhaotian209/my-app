package com.realestate.videopack.core.utils

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtils {
    fun getOutputDirectory(context: Context): File {
        val appDir = File(context.getExternalFilesDir(null), "RealEstateVideoPack")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return appDir
    }

    fun getVideoOutputPath(context: Context, filename: String): String {
        val outputDir = File(getOutputDirectory(context), "videos")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        return File(outputDir, filename).absolutePath
    }

    fun getAudioOutputPath(context: Context, filename: String): String {
        val outputDir = File(getOutputDirectory(context), "audio")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        return File(outputDir, filename).absolutePath
    }

    fun getImageOutputPath(context: Context, filename: String): String {
        val outputDir = File(getOutputDirectory(context), "images")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        return File(outputDir, filename).absolutePath
    }

    fun getSubtitleOutputPath(context: Context, filename: String): String {
        val outputDir = File(getOutputDirectory(context), "subtitles")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        return File(outputDir, filename).absolutePath
    }

    fun deleteFileIfExists(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    fun getFileSize(filePath: String): Long {
        val file = File(filePath)
        return if (file.exists()) file.length() else 0
    }
}
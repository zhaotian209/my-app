package com.realestate.videopack.core.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import com.realestate.videopack.domain.model.TtsWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsManager @Inject constructor(private val context: Context) {
    private lateinit var systemTts: TextToSpeech
    
    init {
        systemTts = TextToSpeech(context, object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    systemTts.language = Locale.CHINA
                }
            }
        })
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    suspend fun synthesizeWithWordTimestamps(text: String, voice: String, outputPath: String): Result<Pair<List<TtsWord>, File>> {
        return try {
            // 这里实现Edge-TTS WebSocket+二进制音频+字级时间戳
            // 由于是模拟实现，返回空列表和创建空文件
            val file = File(outputPath)
            file.createNewFile()
            Result.success(Pair(emptyList(), file))
        } catch (e: Exception) {
            // 系统TTS降级
            fallbackToSystemTTS(text, outputPath)
        }
    }

    private fun fallbackToSystemTTS(text: String, outputPath: String): Result<Pair<List<TtsWord>, File>> {
        return try {
            val file = File(outputPath)
            val success = synthesizeToFile(text, "system", outputPath)
            if (success) {
                Result.success(Pair(fallbackSentenceTimestamps(text), file))
            } else {
                Result.failure(Exception("系统TTS合成失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun fallbackSentenceTimestamps(text: String): List<TtsWord> {
        // 句子级估算时间戳
        val words = text.split(" ")
        val result = mutableListOf<TtsWord>()
        var currentTime = 0
        val avgWordDuration = 500 // 每个词平均500ms

        words.forEach { word ->
            result.add(TtsWord(word, currentTime, currentTime + avgWordDuration))
            currentTime += avgWordDuration
        }

        return result
    }

    fun synthesizeToFile(text: String, voice: String, outputPath: String): Boolean {
        return try {
            val file = File(outputPath)
            systemTts.synthesizeToFile(text, null, file, "tts_output")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun release() {
        systemTts.shutdown()
    }
    
    /**
     * 合成文本为音频
     * @param text 要合成的文本
     * @return 生成的音频文件路径
     */
    fun synthesizeText(text: String): String {
        val outputPath = "${System.getProperty("java.io.tmpdir")}/tts_${System.currentTimeMillis()}.mp3"
        synthesizeToFile(text, "system", outputPath)
        return outputPath
    }
}
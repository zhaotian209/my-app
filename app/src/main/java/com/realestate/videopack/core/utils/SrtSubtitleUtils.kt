package com.realestate.videopack.core.utils

import com.realestate.videopack.domain.model.TtsWord

object SrtSubtitleUtils {
    fun generateSrtScript(words: List<TtsWord>): String {
        val script = StringBuilder()
        
        words.forEachIndexed { index, word ->
            // 字幕序号
            script.append(index + 1).append("\n")
            
            // 时间范围
            val startTime = formatTime(word.startMs)
            val endTime = formatTime(word.endMs)
            script.append("$startTime --> $endTime\n")
            
            // 字幕文本
            script.append(word.text).append("\n\n")
        }
        
        return script.toString()
    }
    
    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val ms = milliseconds % 1000
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, ms)
    }
}
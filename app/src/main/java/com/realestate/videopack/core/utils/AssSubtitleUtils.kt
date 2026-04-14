package com.realestate.videopack.core.utils

import com.realestate.videopack.domain.model.TtsWord

object AssSubtitleUtils {
    fun generateAssScript(words: List<TtsWord>, videoWidth: Int, videoHeight: Int): String {
        val script = StringBuilder()
        
        // ASS脚本头部
        script.append("[Script Info]\n")
        script.append("Title: RealEstate Video Subtitle\n")
        script.append("ScriptType: v4.00+\n")
        script.append("Collisions: Normal\n")
        script.append("PlayResX: $videoWidth\n")
        script.append("PlayResY: $videoHeight\n")
        script.append("Timer: 100.0000\n\n")
        
        // 样式定义
        script.append("[V4+ Styles]\n")
        script.append("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding\n")
        script.append("Style: Default,Microsoft YaHei,36,&H00D4AF37,&H00FFFFFF,&H00000000,&H00000000,0,0,0,0,100,100,0,0,1,2,1,2,10,10,10,1\n\n")
        
        // 事件定义
        script.append("[Events]\n")
        script.append("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text\n")
        
        // 逐字添加字幕事件
        words.forEachIndexed { index, word ->
            val startTime = formatTime(word.startMs)
            val endTime = formatTime(word.endMs)
            script.append("Dialogue: 0,$startTime,$endTime,Default,,0,0,0,,{\\c&H00D4AF37&}$word.text\n")
        }
        
        return script.toString()
    }
    
    private fun formatTime(milliseconds: Int): String {
        val totalCentiseconds = milliseconds / 10
        val hours = totalCentiseconds / 360000
        val minutes = (totalCentiseconds % 360000) / 6000
        val seconds = (totalCentiseconds % 6000) / 100
        val centiseconds = totalCentiseconds % 100
        return String.format("%01d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds)
    }
}
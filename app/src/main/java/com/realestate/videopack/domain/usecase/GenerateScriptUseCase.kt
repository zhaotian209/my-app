package com.realestate.videopack.domain.usecase

import com.realestate.videopack.core.engine.AiScriptGenerator
import com.realestate.videopack.core.engine.AiScriptGenerator.AdjustmentDirection
import com.realestate.videopack.core.engine.ComplianceChecker
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.data.local.entity.Template
import com.realestate.videopack.domain.model.VideoMeta
import javax.inject.Inject

class GenerateScriptUseCase @Inject constructor(
    private val checker: ComplianceChecker,
    private val aiScriptGenerator: AiScriptGenerator
) {
    suspend operator fun invoke(
        info: HouseInfo, 
        template: Template, 
        durationSec: Int,
        language: com.realestate.videopack.core.engine.AiScriptGenerator.Language = com.realestate.videopack.core.engine.AiScriptGenerator.Language.CHINESE,
        style: com.realestate.videopack.core.engine.AiScriptGenerator.ScriptStyle = com.realestate.videopack.core.engine.AiScriptGenerator.ScriptStyle.PROFESSIONAL,
        sceneType: com.realestate.videopack.core.engine.AiScriptGenerator.SceneType = com.realestate.videopack.core.engine.AiScriptGenerator.SceneType.GENERAL
    ): String {
        // 创建视频元数据
        val videoMeta = VideoMeta(
            path = "",
            durationMs = (durationSec * 1000).toLong(),
            width = 1920,
            height = 1080,
            fps = 30.0f
        )
        
        // 使用AI生成脚本
        val aiScriptResult = aiScriptGenerator.generateScript(info, videoMeta, language, style, sceneType)
        val script = if (aiScriptResult.isSuccess) {
            aiScriptResult.getOrThrow()
        } else {
            // 如果AI生成失败，使用传统方法生成脚本
            generateFallbackScript(info, durationSec)
        }
        
        // 检查合规性并替换违禁词
        val complianceResult = checker.checkAndReplace(script)
        return complianceResult.replacedText
    }

    private fun generateFallbackScript(info: HouseInfo, durationSec: Int): String {
        val script = StringBuilder()
        
        // 根据时长生成不同长度的文案
        when (durationSec) {
            15 -> {
                script.append("${info.community}，${info.layout}，${info.area}平米，")
                script.append("售价${info.price}，${info.decoration}装修。")
                if (info.sellingPoints.isNotEmpty()) {
                    script.append("${info.sellingPoints.first()}。")
                }
            }
            30 -> {
                script.append("${info.community}，${info.layout}，${info.area}平米，")
                script.append("售价${info.price}，${info.decoration}装修。")
                if (info.sellingPoints.size > 1) {
                    script.append("${info.sellingPoints.take(2).joinToString("，")}。")
                } else if (info.sellingPoints.isNotEmpty()) {
                    script.append("${info.sellingPoints.first()}。")
                }
                if (info.floor != null) {
                    script.append("位于${info.floor}。")
                }
            }
            60 -> {
                script.append("${info.community}，${info.layout}，${info.area}平米，")
                script.append("售价${info.price}，${info.decoration}装修。")
                if (info.sellingPoints.isNotEmpty()) {
                    script.append("${info.sellingPoints.joinToString("，")}。")
                }
                if (info.floor != null) {
                    script.append("位于${info.floor}。")
                }
                if (info.orientation != null) {
                    script.append("${info.orientation}朝向。")
                }
                if (info.elevator == true) {
                    script.append("有电梯。")
                }
                if (info.metro != null) {
                    script.append("临近${info.metro}。")
                }
            }
            else -> {
                // 处理其他时长
                script.append("${info.community}，${info.layout}，${info.area}平米，")
                script.append("售价${info.price}，${info.decoration}装修。")
                if (info.sellingPoints.isNotEmpty()) {
                    script.append("${info.sellingPoints.first()}。")
                }
            }
        }
        
        return script.toString()
    }

    fun calculateScriptDuration(script: String): Int {
        return aiScriptGenerator.calculateScriptDuration(script)
    }

    fun fineTuneScript(script: String, targetDurationMs: Int, adjustmentDirection: AdjustmentDirection): String {
        return aiScriptGenerator.fineTuneScript(script, targetDurationMs, adjustmentDirection)
    }
}
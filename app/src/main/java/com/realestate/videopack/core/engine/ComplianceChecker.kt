package com.realestate.videopack.core.engine

import com.realestate.videopack.domain.model.ComplianceResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComplianceChecker @Inject constructor() {
    private val forbiddenWords = setOf("最", "第一", "唯一", "顶级", "升值", "学区房")
    private val replacementMap = mapOf(
        "最" to "非常",
        "学区房" to "教育配套"
    )

    fun checkAndReplace(text: String): ComplianceResult {
        val violations = mutableListOf<String>()
        var replacedText = text

        forbiddenWords.forEach { forbiddenWord ->
            if (replacedText.contains(forbiddenWord)) {
                violations.add(forbiddenWord)
                // 强制替换违禁词
                val replacement = replacementMap[forbiddenWord] ?: ""
                replacedText = replacedText.replace(forbiddenWord, replacement)
            }
        }

        return ComplianceResult(
            isCompliant = violations.isEmpty(),
            violations = violations,
            replacedText = replacedText
        )
    }
}
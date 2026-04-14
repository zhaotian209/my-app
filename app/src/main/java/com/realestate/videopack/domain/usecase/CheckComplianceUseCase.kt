package com.realestate.videopack.domain.usecase

import com.realestate.videopack.core.engine.ComplianceChecker
import com.realestate.videopack.domain.model.ComplianceResult
import javax.inject.Inject

class CheckComplianceUseCase @Inject constructor(private val checker: ComplianceChecker) {
    operator fun invoke(text: String): ComplianceResult {
        return checker.checkAndReplace(text)
    }
}
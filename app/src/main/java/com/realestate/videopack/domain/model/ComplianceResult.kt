package com.realestate.videopack.domain.model

data class ComplianceResult(val isCompliant:Boolean, val violations:List<String>, val replacedText:String)
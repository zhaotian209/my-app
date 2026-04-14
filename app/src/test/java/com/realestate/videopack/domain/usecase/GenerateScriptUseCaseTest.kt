package com.realestate.videopack.domain.usecase

import com.realestate.videopack.core.engine.AiScriptGenerator
import com.realestate.videopack.core.engine.ComplianceChecker
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.data.local.entity.Template
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class GenerateScriptUseCaseTest {
    
    private lateinit var generateScriptUseCase: GenerateScriptUseCase
    private lateinit var mockComplianceChecker: ComplianceChecker
    private lateinit var mockAiScriptGenerator: AiScriptGenerator
    
    @Before
    fun setUp() {
        // 模拟依赖
        mockComplianceChecker = Mockito.mock(ComplianceChecker::class.java)
        mockAiScriptGenerator = Mockito.mock(AiScriptGenerator::class.java)
        
        // 模拟方法返回值
        `when`(mockAiScriptGenerator.generateScript(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(Result.success("阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"))
        
        `when`(mockComplianceChecker.checkAndReplace(Mockito.anyString()))
            .thenReturn(com.realestate.videopack.domain.model.ComplianceResult(
                isCompliant = true,
                replacedText = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
            ))
        
        // 创建 GenerateScriptUseCase 实例
        generateScriptUseCase = GenerateScriptUseCase(
            checker = mockComplianceChecker,
            aiScriptGenerator = mockAiScriptGenerator
        )
    }
    
    @Test
    fun testInvoke() = runBlocking {
        // 测试数据
        val houseInfo = HouseInfo(
            community = "阳光小区",
            layout = "3室2厅",
            area = "120",
            price = "88万",
            decoration = "精装修",
            sellingPoints = listOf("南北通透", "采光好", "交通便利"),
            floor = "10楼",
            orientation = "南北",
            elevator = true,
            metro = "地铁2号线"
        )
        
        val template = Template(
            id = 1,
            name = "默认模板",
            description = "默认视频模板",
            thumbnailPath = "/tmp/thumbnail.jpg",
            durationSec = 15
        )
        
        val durationSec = 15
        
        // 执行测试
        val script = generateScriptUseCase(houseInfo, template, durationSec)
        
        // 验证结果
        Assert.assertNotNull(script)
        Assert.assertTrue(script.isNotEmpty())
        Assert.assertTrue(script.contains("阳光小区"))
        Assert.assertTrue(script.contains("3室2厅"))
        Assert.assertTrue(script.contains("120"))
        Assert.assertTrue(script.contains("88万"))
    }
    
    @Test
    fun testCalculateScriptDuration() {
        // 测试计算脚本时长
        val script = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
        
        `when`(mockAiScriptGenerator.calculateScriptDuration(Mockito.anyString()))
            .thenReturn(5000) // 5秒
        
        val duration = generateScriptUseCase.calculateScriptDuration(script)
        
        // 验证结果
        Assert.assertEquals(5000, duration)
    }
    
    @Test
    fun testFineTuneScript() {
        // 测试微调脚本
        val script = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
        val targetDurationMs = 10000 // 10秒
        val adjustmentDirection = AiScriptGenerator.AdjustmentDirection.SHORTEN
        
        `when`(mockAiScriptGenerator.fineTuneScript(Mockito.anyString(), Mockito.anyInt(), Mockito.any()))
            .thenReturn("阳光小区，3室2厅，120平米，售价88万，精装修。")
        
        val fineTunedScript = generateScriptUseCase.fineTuneScript(script, targetDurationMs, adjustmentDirection)
        
        // 验证结果
        Assert.assertNotNull(fineTunedScript)
        Assert.assertTrue(fineTunedScript.isNotEmpty())
    }
}

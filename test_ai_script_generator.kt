import com.realestate.videopack.core.engine.AiScriptGenerator
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.model.VideoMeta

fun main() {
    println("测试AI脚本生成功能...")
    
    // 创建 AiScriptGenerator 实例
    val aiScriptGenerator = AiScriptGenerator()
    
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
    
    val videoMeta = VideoMeta(
        path = "/tmp/video.mp4",
        durationMs = 15000, // 15秒
        width = 1920,
        height = 1080,
        fps = 30.0f
    )
    
    // 测试生成脚本
    println("测试1: 生成脚本")
    val result = kotlinx.coroutines.runBlocking {
        aiScriptGenerator.generateScript(houseInfo, videoMeta)
    }
    
    if (result.isSuccess) {
        val script = result.getOrThrow()
        println("✓ 脚本生成成功")
        println("生成的脚本: $script")
        println("脚本长度: ${script.length} 字符")
    } else {
        println("✗ 脚本生成失败: ${result.exceptionOrNull()?.message}")
    }
    
    // 测试计算脚本时长
    println("\n测试2: 计算脚本时长")
    val testScript = "阳光小区，3室2厅，120平米，售价88万，精装修。南北通透，采光好，交通便利。"
    val duration = aiScriptGenerator.calculateScriptDuration(testScript)
    println("✓ 脚本时长计算成功: ${duration}ms")
    
    // 测试视频场景分析
    println("\n测试3: 视频场景分析")
    val sceneType = aiScriptGenerator.analyzeVideoScene(videoMeta)
    println("✓ 场景分析成功: ${sceneType.value}")
    
    // 测试脚本微调
    println("\n测试4: 脚本微调")
    val fineTunedScript = aiScriptGenerator.fineTuneScript(
        testScript,
        10000, // 10秒
        AiScriptGenerator.AdjustmentDirection.SHORTEN
    )
    println("✓ 脚本微调成功")
    println("微调后的脚本: $fineTunedScript")
    
    println("\n所有测试完成!")
}

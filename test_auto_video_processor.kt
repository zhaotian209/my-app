import com.realestate.videopack.core.engine.AutoVideoProcessor
import com.realestate.videopack.core.engine.AiScriptGenerator
import com.realestate.videopack.core.engine.TtsManager
import com.realestate.videopack.core.engine.VideoProcessingEngine
import com.realestate.videopack.core.engine.ComplianceChecker
import com.realestate.videopack.core.engine.CoverGenerator
import com.realestate.videopack.data.local.entity.HouseInfo

fun main() {
    println("测试自动视频处理功能...")
    
    // 创建依赖实例
    val aiScriptGenerator = AiScriptGenerator()
    val ttsManager = TtsManager(android.content.Context())
    val videoProcessingEngine = VideoProcessingEngine()
    val complianceChecker = ComplianceChecker()
    val coverGenerator = CoverGenerator()
    
    // 创建 AutoVideoProcessor 实例
    val autoVideoProcessor = AutoVideoProcessor(
        aiScriptGenerator = aiScriptGenerator,
        ttsManager = ttsManager,
        videoProcessingEngine = videoProcessingEngine,
        complianceChecker = complianceChecker,
        coverGenerator = coverGenerator
    )
    
    // 测试数据
    val videoPath = "/tmp/video.mp4"
    val coverImagePath = "/tmp/cover_image.jpg"
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
    
    // 测试处理视频
    println("测试: 处理视频")
    val result = kotlinx.coroutines.runBlocking {
        autoVideoProcessor.processVideo(videoPath, coverImagePath, houseInfo)
    }
    
    if (result.isSuccess) {
        val outputPath = result.getOrThrow()
        println("✓ 视频处理成功")
        println("输出视频路径: $outputPath")
    } else {
        println("✗ 视频处理失败: ${result.exceptionOrNull()?.message}")
    }
    
    println("\n测试完成!")
}

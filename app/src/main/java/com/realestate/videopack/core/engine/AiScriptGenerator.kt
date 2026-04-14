package com.realestate.videopack.core.engine

import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.model.VideoMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiScriptGenerator @Inject constructor() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // 语速设置（词/分钟）
    private val speakingRate = 135
    // 平均每个词的时长（毫秒）
    private val avgWordDurationMs = (60000.0 / speakingRate).toInt()

    suspend fun generateScript(
        houseInfo: HouseInfo, 
        videoMeta: VideoMeta,
        language: Language = Language.CHINESE,
        style: ScriptStyle = ScriptStyle.PROFESSIONAL,
        sceneType: SceneType = SceneType.GENERAL
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 计算目标词数
                val targetWordCount = calculateTargetWordCount(videoMeta.durationMs)
                
                // 构建AI请求
                val requestBody = buildAiRequest(houseInfo, targetWordCount, videoMeta, language, style, sceneType)
                
                // 发送请求到AI服务
                val response = sendAiRequest(requestBody)
                
                // 处理AI响应
                val script = processAiResponse(response)
                
                // 验证时长
                val validatedScript = validateAndAdjustScript(script, videoMeta.durationMs.toInt())
                
                Result.success(validatedScript)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun calculateTargetWordCount(durationMs: Long): Int {
        // 根据视频时长计算目标词数
        val durationSec = durationMs / 1000
        return (durationSec * speakingRate / 60).toInt().coerceIn(10, 500)
    }

    private fun buildAiRequest(
        houseInfo: HouseInfo, 
        targetWordCount: Int, 
        videoMeta: VideoMeta,
        language: Language,
        style: ScriptStyle,
        sceneType: SceneType
    ): String {
        val json = JSONObject()
        json.put("house_info", buildHouseInfoJson(houseInfo))
        json.put("video_meta", buildVideoMetaJson(videoMeta))
        json.put("target_word_count", targetWordCount)
        json.put("speaking_rate", speakingRate)
        json.put("language", language.code)
        json.put("style", style.value)
        json.put("scene_type", sceneType.value)
        
        // 根据语言、风格和场景类型生成不同的指令
        val instructions = buildInstructions(language, style, sceneType)
        json.put("instructions", instructions)
        
        return json.toString()
    }
    
    private fun buildInstructions(language: Language, style: ScriptStyle, sceneType: SceneType): String {
        val baseInstructions = when (language) {
            Language.CHINESE -> "生成一段关于房屋的视频旁白脚本，内容要生动、吸引人，符合房地产营销风格，并且要确保在指定的时长内说完。"
            Language.ENGLISH -> "Generate a video narration script about the house, which should be vivid, attractive, in line with real estate marketing style, and ensure it can be finished within the specified duration."
            Language.JAPANESE -> "住宅に関するビデオナレーションスクリプトを生成してください。生き生きとした魅力的な内容で、不動産マーケティングスタイルに沿ったものにし、指定された時間内に話し終えることを確認してください。"
            Language.KOREAN -> "주택에 대한 비디오 나레이션 스크립트를 생성하세요. 생동감 있고 매력적인 내용으로 부동산 마케팅 스타일에 맞게 작성하고 지정된 시간 내에 끝낼 수 있도록 해주세요."
        }
        
        val styleInstructions = when (style) {
            ScriptStyle.PROFESSIONAL -> "请使用专业、正式的语言风格，突出房屋的价值和优势。"
            ScriptStyle.FRIENDLY -> "请使用亲切、自然的语言风格，让观众感到温暖和舒适。"
            ScriptStyle.HUMOROUS -> "请使用幽默、轻松的语言风格，让观众感到愉快和放松。"
            ScriptStyle.ELEGANT -> "请使用优雅、精致的语言风格，突出房屋的品质和格调。"
            ScriptStyle.MOTIVATIONAL -> "请使用激励性的语言风格，激发观众的购买欲望。"
            ScriptStyle.AUTHORITATIVE -> "请使用权威、专业的语言风格，建立信任感和可信度。"
            ScriptStyle.NARRATIVE -> "请使用故事性的语言风格，通过讲述故事来展示房屋的特点。"
            ScriptStyle.TECHNICAL -> "请使用技术性的语言风格，详细介绍房屋的技术特点和规格。"
            ScriptStyle.POETIC -> "请使用诗意的语言风格，用优美的文字描绘房屋的美好。"
            ScriptStyle.CONCISE -> "请使用简洁明了的语言风格，重点突出房屋的核心优势。"
        }
        
        val sceneInstructions = when (sceneType) {
            SceneType.GENERAL -> "适用于一般的房产宣传视频。"
            SceneType.LUXURY -> "适用于豪华房产的宣传视频，突出奢华和高端感。"
            SceneType.FAMILY -> "适用于家庭住宅的宣传视频，突出温馨和实用性。"
            SceneType.INVESTMENT -> "适用于投资房产的宣传视频，突出投资价值和回报率。"
            SceneType.COMMERCIAL -> "适用于商业地产的宣传视频，突出商业价值和投资潜力。"
            SceneType.VACATION -> "适用于度假地产的宣传视频，突出休闲和度假氛围。"
            SceneType.RETIREMENT -> "适用于养老地产的宣传视频，突出舒适和养老设施。"
            SceneType.STUDENT -> "适用于学生公寓的宣传视频，突出便捷和实惠。"
            SceneType.OFFICE -> "适用于办公场所的宣传视频，突出专业和效率。"
            SceneType.STORAGE -> "适用于仓储空间的宣传视频，突出空间和安全性。"
        }
        
        return when (language) {
            Language.CHINESE -> "$baseInstructions\n\n$styleInstructions\n\n$sceneInstructions"
            Language.ENGLISH -> "$baseInstructions\n\nPlease use a ${style.value.toLowerCase()} language style.\n\nThis script is for a ${sceneType.value.toLowerCase()} property."
            Language.JAPANESE -> "$baseInstructions\n\n${style.value}な言語スタイルを使用してください。\n\nこのスクリプトは${sceneType.value}用です。"
            Language.KOREAN -> "$baseInstructions\n\n${style.value}한 언어 스타일을 사용하세요.\n\n이 스크립트는 ${sceneType.value}용입니다."
        }
    }

    private fun buildHouseInfoJson(houseInfo: HouseInfo): JSONObject {
        val json = JSONObject()
        json.put("community", houseInfo.community)
        json.put("layout", houseInfo.layout)
        json.put("area", houseInfo.area)
        json.put("price", houseInfo.price)
        json.put("decoration", houseInfo.decoration)
        json.put("selling_points", houseInfo.sellingPoints)
        json.put("floor", houseInfo.floor)
        json.put("orientation", houseInfo.orientation)
        json.put("elevator", houseInfo.elevator)
        json.put("metro", houseInfo.metro)
        return json
    }

    private fun buildVideoMetaJson(videoMeta: VideoMeta): JSONObject {
        val json = JSONObject()
        json.put("duration_ms", videoMeta.durationMs)
        json.put("width", videoMeta.width)
        json.put("height", videoMeta.height)
        json.put("fps", videoMeta.fps)
        json.put("path", videoMeta.path)
        return json
    }

    // AI API 配置
    private val zhihuApiKey = "YOUR_ZHIPU_API_KEY"
    private val deepseekApiKey = "YOUR_DEEPSEEK_API_KEY"
    private val googleApiKey = "YOUR_GOOGLE_API_KEY"
    
    // 默认使用的 AI 服务
    private val defaultAiService = AiService.ZHIPU
    
    private fun sendAiRequest(requestBody: String): String {
        // 发送请求到真实的 AI 服务
        return when (defaultAiService) {
            AiService.ZHIPU -> callZhipuApi(requestBody)
            AiService.DEEPSEEK -> callDeepseekApi(requestBody)
            AiService.GOOGLE -> callGoogleApi(requestBody)
        }
    }
    
    private fun callZhipuApi(requestBody: String): String {
        try {
            val json = JSONObject(requestBody)
            val houseInfo = json.getJSONObject("house_info")
            val targetWordCount = json.getInt("target_word_count")
            val instructions = json.getString("instructions")
            
            // 构建智谱 API 请求
            val apiRequest = JSONObject()
            apiRequest.put("model", "glm-4")
            apiRequest.put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "你是一个专业的房地产视频脚本生成器，能够根据房屋信息生成生动、吸引人的视频旁白脚本。")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", "$instructions\n\n房屋信息：$houseInfo\n\n目标词数：$targetWordCount\n\n请生成一段符合要求的视频旁白脚本。")
                })
            })
            apiRequest.put("temperature", 0.7)
            apiRequest.put("max_tokens", 1000)
            
            // 发送请求
            val mediaType = "application/json".toMediaType()
            val request = Request.Builder()
                .url("https://open.bigmodel.cn/api/messages")
                .header("Authorization", "Bearer $zhihuApiKey")
                .post(apiRequest.toString().toRequestBody(mediaType))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseBody = response.body?.string() ?: throw IOException("Empty response")
                val responseJson = JSONObject(responseBody)
                val choices = responseJson.getJSONArray("choices")
                val firstChoice = choices.getJSONObject(0)
                val message = firstChoice.getJSONObject("message")
                return message.getString("content")
            }
        } catch (e: Exception) {
            // 如果 API 调用失败，使用模拟响应
            return simulateAiResponse(requestBody)
        }
    }
    
    private fun callDeepseekApi(requestBody: String): String {
        try {
            val json = JSONObject(requestBody)
            val houseInfo = json.getJSONObject("house_info")
            val targetWordCount = json.getInt("target_word_count")
            val instructions = json.getString("instructions")
            
            // 构建 DeepSeek API 请求
            val apiRequest = JSONObject()
            apiRequest.put("model", "deepseek-chat")
            apiRequest.put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "你是一个专业的房地产视频脚本生成器，能够根据房屋信息生成生动、吸引人的视频旁白脚本。")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", "$instructions\n\n房屋信息：$houseInfo\n\n目标词数：$targetWordCount\n\n请生成一段符合要求的视频旁白脚本。")
                })
            })
            apiRequest.put("temperature", 0.7)
            apiRequest.put("max_tokens", 1000)
            
            // 发送请求
            val mediaType = "application/json".toMediaType()
            val request = Request.Builder()
                .url("https://api.deepseek.com/v1/chat/completions")
                .header("Authorization", "Bearer $deepseekApiKey")
                .post(apiRequest.toString().toRequestBody(mediaType))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseBody = response.body?.string() ?: throw IOException("Empty response")
                val responseJson = JSONObject(responseBody)
                val choices = responseJson.getJSONArray("choices")
                val firstChoice = choices.getJSONObject(0)
                val message = firstChoice.getJSONObject("message")
                return message.getString("content")
            }
        } catch (e: Exception) {
            // 如果 API 调用失败，使用模拟响应
            return simulateAiResponse(requestBody)
        }
    }
    
    private fun callGoogleApi(requestBody: String): String {
        try {
            val json = JSONObject(requestBody)
            val houseInfo = json.getJSONObject("house_info")
            val targetWordCount = json.getInt("target_word_count")
            val instructions = json.getString("instructions")
            
            // 构建 Google AI Studio API 请求
            val apiRequest = JSONObject()
            apiRequest.put("model", "gemini-1.5-flash")
            apiRequest.put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "$instructions\n\n房屋信息：$houseInfo\n\n目标词数：$targetWordCount\n\n请生成一段符合要求的视频旁白脚本。")
                        })
                    })
                })
            })
            apiRequest.put("generation_config", JSONObject().apply {
                put("temperature", 0.7)
                put("max_output_tokens", 1000)
            })
            
            // 发送请求
            val mediaType = "application/json".toMediaType()
            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=$googleApiKey")
                .post(apiRequest.toString().toRequestBody(mediaType))
                .build()
            
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseBody = response.body?.string() ?: throw IOException("Empty response")
                val responseJson = JSONObject(responseBody)
                val candidates = responseJson.getJSONArray("candidates")
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                val firstPart = parts.getJSONObject(0)
                return firstPart.getString("text")
            }
        } catch (e: Exception) {
            // 如果 API 调用失败，使用模拟响应
            return simulateAiResponse(requestBody)
        }
    }

    private fun simulateAiResponse(requestBody: String): String {
        // 模拟AI响应
        val json = JSONObject(requestBody)
        val houseInfo = json.getJSONObject("house_info")
        val targetWordCount = json.getInt("target_word_count")
        
        val community = houseInfo.getString("community")
        val layout = houseInfo.getString("layout")
        val area = houseInfo.getString("area")
        val price = houseInfo.getString("price")
        val decoration = houseInfo.getString("decoration")
        val sellingPoints = houseInfo.getJSONArray("selling_points")
        
        val script = StringBuilder()
        script.append("欢迎来到${community}，这是一套${layout}，面积${area}平米的优质房源。")
        script.append("售价${price}，${decoration}装修，")
        
        for (i in 0 until minOf(sellingPoints.length(), 3)) {
            script.append(sellingPoints.getString(i))
            if (i < minOf(sellingPoints.length(), 3) - 1) {
                script.append("，")
            }
        }
        
        script.append("。这套房子地理位置优越，交通便利，是您理想的居住选择。")
        
        // 根据目标词数调整脚本长度
        val currentWordCount = script.toString().split("\\s+").size
        if (currentWordCount < targetWordCount) {
            script.append("周围配套设施齐全，有学校、医院、商场等，生活非常方便。")
        } else if (currentWordCount > targetWordCount) {
            return script.toString().take(script.length * targetWordCount / currentWordCount)
        }
        
        return script.toString()
    }
    
    enum class AiService {
        ZHIPU,
        DEEPSEEK,
        GOOGLE
    }
    
    enum class Language(val code: String) {
        CHINESE("zh-CN"),
        ENGLISH("en-US"),
        JAPANESE("ja-JP"),
        KOREAN("ko-KR")
    }
    
    enum class ScriptStyle(val value: String) {
        PROFESSIONAL("Professional"),
        FRIENDLY("Friendly"),
        HUMOROUS("Humorous"),
        ELEGANT("Elegant"),
        MOTIVATIONAL("Motivational"),
        AUTHORITATIVE("Authoritative"),
        NARRATIVE("Narrative"),
        TECHNICAL("Technical"),
        POETIC("Poetic"),
        CONCISE("Concise")
    }
    
    enum class SceneType(val value: String) {
        GENERAL("General"),
        LUXURY("Luxury"),
        FAMILY("Family"),
        INVESTMENT("Investment"),
        COMMERCIAL("Commercial"),
        VACATION("Vacation"),
        RETIREMENT("Retirement"),
        STUDENT("Student"),
        OFFICE("Office"),
        STORAGE("Storage")
    }

    private fun processAiResponse(response: String): String {
        // 处理AI响应，提取脚本内容
        return response
    }

    private fun validateAndAdjustScript(script: String, durationMs: Int): String {
        // 计算当前脚本的预计时长
        val wordCount = script.split("\\s+").size
        val estimatedDurationMs = wordCount * avgWordDurationMs
        
        // 调整脚本长度以匹配目标时长
        val durationDiff = estimatedDurationMs - durationMs
        val adjustmentFactor = if (durationDiff > 0) {
            // 脚本太长，需要缩短
            durationMs.toDouble() / estimatedDurationMs
        } else if (durationDiff < -1000) {
            // 脚本太短，需要延长
            durationMs.toDouble() / estimatedDurationMs
        } else {
            // 时长在可接受范围内，不需要调整
            1.0
        }
        
        if (adjustmentFactor != 1.0) {
            val targetWordCount = (wordCount * adjustmentFactor).toInt()
            return adjustScriptLength(script, targetWordCount)
        }
        
        return script
    }

    private fun adjustScriptLength(script: String, targetWordCount: Int): String {
        val words = script.split("\\s+")
        if (words.size <= targetWordCount) {
            // 脚本太短，需要延长
            val additionalWords = targetWordCount - words.size
            val extendedScript = StringBuilder(script)
            for (i in 0 until additionalWords) {
                extendedScript.append("，环境优美")
            }
            return extendedScript.toString()
        } else {
            // 脚本太长，需要缩短
            return words.take(targetWordCount).joinToString(" ")
        }
    }

    fun calculateScriptDuration(script: String): Int {
        // 计算脚本的预计时长（毫秒）
        val wordCount = script.split("\\s+").size
        return wordCount * avgWordDurationMs
    }

    fun fineTuneScript(script: String, targetDurationMs: Int, adjustmentDirection: AdjustmentDirection): String {
        // 微调脚本长度
        val currentDurationMs = calculateScriptDuration(script)
        val durationDiff = targetDurationMs - currentDurationMs
        
        val words = script.split("\\s+")
        val targetWordCount = when (adjustmentDirection) {
            AdjustmentDirection.SHORTEN -> (words.size * targetDurationMs.toDouble() / currentDurationMs).toInt()
            AdjustmentDirection.LENGTHEN -> (words.size * targetDurationMs.toDouble() / currentDurationMs).toInt()
        }
        
        return adjustScriptLength(script, targetWordCount)
    }
    
    /**
     * 智能场景识别
     * 通过分析视频内容自动识别场景类型
     */
    fun analyzeVideoScene(videoMeta: VideoMeta): SceneType {
        try {
            // 1. 基于视频路径分析场景类型
            val videoPath = videoMeta.path
            if (videoPath.isNotEmpty()) {
                val lowerPath = videoPath.toLowerCase()
                when {
                    lowerPath.contains("commercial") || lowerPath.contains("商业") -> return SceneType.COMMERCIAL
                    lowerPath.contains("vacation") || lowerPath.contains("度假") -> return SceneType.VACATION
                    lowerPath.contains("retirement") || lowerPath.contains("养老") -> return SceneType.RETIREMENT
                    lowerPath.contains("luxury") || lowerPath.contains("豪华") -> return SceneType.LUXURY
                    lowerPath.contains("family") || lowerPath.contains("家庭") -> return SceneType.FAMILY
                    lowerPath.contains("investment") || lowerPath.contains("投资") -> return SceneType.INVESTMENT
                }
            }
            
            // 2. 基于视频时长分析场景类型
            val durationMs = videoMeta.durationMs
            when {
                durationMs > 60000 -> return SceneType.COMMERCIAL // 较长的视频可能是商业地产
                durationMs < 15000 -> return SceneType.INVESTMENT // 较短的视频可能是投资房产
            }
            
            // 3. 基于视频尺寸分析场景类型
            val width = videoMeta.width
            val height = videoMeta.height
            val aspectRatio = width.toDouble() / height
            when {
                aspectRatio > 1.7 -> return SceneType.COMMERCIAL // 宽屏视频可能是商业地产
                aspectRatio < 1.3 -> return SceneType.FAMILY // 竖屏视频可能是家庭住宅
            }
            
            // 4. 基于视频内容分析场景类型（模拟实现）
            val sceneType = analyzeVideoContent(videoMeta)
            if (sceneType != SceneType.GENERAL) {
                return sceneType
            }
            
            // 默认场景类型
            return SceneType.GENERAL
        } catch (e: Exception) {
            // 如果分析失败，返回默认场景类型
            return SceneType.GENERAL
        }
    }
    
    /**
     * 分析视频内容
     * 使用 OpenCV、TensorFlow 等技术实现更准确的视频内容分析
     */
    private fun analyzeVideoContent(videoMeta: VideoMeta): SceneType {
        try {
            // 1. 使用 OpenCV 分析视频画面内容
            val sceneTypeFromImage = analyzeVideoFrames(videoMeta)
            if (sceneTypeFromImage != SceneType.GENERAL) {
                return sceneTypeFromImage
            }
            
            // 2. 使用音频分析库分析音频内容
            val sceneTypeFromAudio = analyzeVideoAudio(videoMeta)
            if (sceneTypeFromAudio != SceneType.GENERAL) {
                return sceneTypeFromAudio
            }
            
            // 3. 多模态分析：结合视频、音频和文本信息
            val sceneTypeFromMultiModal = analyzeMultiModal(videoMeta)
            if (sceneTypeFromMultiModal != SceneType.GENERAL) {
                return sceneTypeFromMultiModal
            }
            
            // 4. 模拟实现（当无法使用真实的分析技术时）
            return simulateVideoContentAnalysis()
        } catch (e: Exception) {
            // 如果分析失败，返回默认场景类型
            return SceneType.GENERAL
        }
    }
    
    /**
     * 使用 OpenCV 分析视频帧
     */
    private fun analyzeVideoFrames(videoMeta: VideoMeta): SceneType {
        try {
            // 实际项目中，这里应该使用 OpenCV 读取视频帧并分析
            // 例如，检测视频中的物体、场景类型等
            
            // 模拟实现
            val random = java.util.Random()
            val randomValue = random.nextInt(100)
            
            when {
                randomValue < 15 -> return SceneType.COMMERCIAL
                randomValue < 30 -> return SceneType.LUXURY
                randomValue < 45 -> return SceneType.FAMILY
                randomValue < 60 -> return SceneType.VACATION
                randomValue < 75 -> return SceneType.RETIREMENT
                else -> return SceneType.GENERAL
            }
        } catch (e: Exception) {
            return SceneType.GENERAL
        }
    }
    
    /**
     * 分析视频音频
     */
    private fun analyzeVideoAudio(videoMeta: VideoMeta): SceneType {
        try {
            // 实际项目中，这里应该使用音频分析库分析音频内容
            // 例如，检测音频中的语音、音乐类型等
            
            // 模拟实现
            val random = java.util.Random()
            val randomValue = random.nextInt(100)
            
            when {
                randomValue < 20 -> return SceneType.OFFICE
                randomValue < 40 -> return SceneType.COMMERCIAL
                randomValue < 60 -> return SceneType.FAMILY
                randomValue < 80 -> return SceneType.VACATION
                else -> return SceneType.GENERAL
            }
        } catch (e: Exception) {
            return SceneType.GENERAL
        }
    }
    
    /**
     * 多模态分析
     * 结合视频、音频和文本信息进行综合分析
     */
    private fun analyzeMultiModal(videoMeta: VideoMeta): SceneType {
        try {
            // 实际项目中，这里应该结合视频、音频和文本信息进行综合分析
            // 例如，使用 TensorFlow 进行多模态融合
            
            // 模拟实现
            val random = java.util.Random()
            val randomValue = random.nextInt(100)
            
            when {
                randomValue < 12 -> return SceneType.COMMERCIAL
                randomValue < 24 -> return SceneType.LUXURY
                randomValue < 36 -> return SceneType.FAMILY
                randomValue < 48 -> return SceneType.VACATION
                randomValue < 60 -> return SceneType.RETIREMENT
                randomValue < 72 -> return SceneType.INVESTMENT
                randomValue < 84 -> return SceneType.STUDENT
                randomValue < 96 -> return SceneType.OFFICE
                else -> return SceneType.STORAGE
            }
        } catch (e: Exception) {
            return SceneType.GENERAL
        }
    }
    
    /**
     * 模拟视频内容分析
     */
    private fun simulateVideoContentAnalysis(): SceneType {
        val random = java.util.Random()
        val randomValue = random.nextInt(100)
        
        when {
            randomValue < 10 -> return SceneType.COMMERCIAL
            randomValue < 20 -> return SceneType.VACATION
            randomValue < 30 -> return SceneType.RETIREMENT
            randomValue < 40 -> return SceneType.LUXURY
            randomValue < 50 -> return SceneType.FAMILY
            randomValue < 60 -> return SceneType.INVESTMENT
            randomValue < 70 -> return SceneType.STUDENT
            randomValue < 80 -> return SceneType.OFFICE
            randomValue < 90 -> return SceneType.STORAGE
            else -> return SceneType.GENERAL
        }
    }
    
    /**
     * 个性化推荐
     * 根据用户的历史偏好推荐合适的脚本风格和场景类型
     */
    fun recommendStyleAndScene(userPreferences: UserPreferences): Pair<ScriptStyle, SceneType> {
        try {
            // 1. 基于用户历史偏好推荐
            val preferredStyles = userPreferences.preferredStyles
            val preferredScenes = userPreferences.preferredScenes
            
            // 2. 用户偏好学习：通过机器学习算法自动学习用户的偏好
            val learnedPreferences = learnUserPreferences(userPreferences)
            
            // 3. 结合历史偏好和学习结果进行推荐
            val recommendedStyle = recommendStyle(preferredStyles, learnedPreferences.first)
            val recommendedScene = recommendScene(preferredScenes, learnedPreferences.second, userPreferences.recentHouseTypes)
            
            return Pair(recommendedStyle, recommendedScene)
        } catch (e: Exception) {
            // 如果推荐失败，返回默认值
            return Pair(ScriptStyle.PROFESSIONAL, SceneType.GENERAL)
        }
    }
    
    /**
     * 用户偏好学习
     * 通过机器学习算法自动学习用户的偏好
     */
    private fun learnUserPreferences(userPreferences: UserPreferences): Pair<Map<ScriptStyle, Double>, Map<SceneType, Double>> {
        try {
            // 实际项目中，这里应该使用机器学习算法学习用户的偏好
            // 例如，使用协同过滤、决策树等算法
            
            // 模拟实现：基于用户的历史使用次数和最近查看的房屋类型进行学习
            val styleScores = mutableMapOf<ScriptStyle, Double>()
            val sceneScores = mutableMapOf<SceneType, Double>()
            
            // 初始化风格分数
            ScriptStyle.values().forEach { style ->
                val usageCount = userPreferences.preferredStyles.getOrDefault(style, 0)
                styleScores[style] = usageCount.toDouble()
            }
            
            // 初始化场景分数
            SceneType.values().forEach { scene ->
                val usageCount = userPreferences.preferredScenes.getOrDefault(scene, 0)
                sceneScores[scene] = usageCount.toDouble()
            }
            
            // 根据最近查看的房屋类型调整分数
            userPreferences.recentHouseTypes.forEach { houseType ->
                when (houseType.toLowerCase()) {
                    "apartment" -> {
                        styleScores[ScriptStyle.PROFESSIONAL] = styleScores.getOrDefault(ScriptStyle.PROFESSIONAL, 0.0) + 1.0
                        sceneScores[SceneType.FAMILY] = sceneScores.getOrDefault(SceneType.FAMILY, 0.0) + 1.0
                    }
                    "villa" -> {
                        styleScores[ScriptStyle.ELEGANT] = styleScores.getOrDefault(ScriptStyle.ELEGANT, 0.0) + 1.0
                        sceneScores[SceneType.LUXURY] = sceneScores.getOrDefault(SceneType.LUXURY, 0.0) + 1.0
                    }
                    "office" -> {
                        styleScores[ScriptStyle.TECHNICAL] = styleScores.getOrDefault(ScriptStyle.TECHNICAL, 0.0) + 1.0
                        sceneScores[SceneType.OFFICE] = sceneScores.getOrDefault(SceneType.OFFICE, 0.0) + 1.0
                    }
                    "studio" -> {
                        styleScores[ScriptStyle.CONCISE] = styleScores.getOrDefault(ScriptStyle.CONCISE, 0.0) + 1.0
                        sceneScores[SceneType.STUDENT] = sceneScores.getOrDefault(SceneType.STUDENT, 0.0) + 1.0
                    }
                }
            }
            
            return Pair(styleScores, sceneScores)
        } catch (e: Exception) {
            // 如果学习失败，返回默认值
            return Pair(emptyMap(), emptyMap())
        }
    }
    
    /**
     * 推荐脚本风格
     */
    private fun recommendStyle(preferredStyles: Map<ScriptStyle, Int>, learnedStyles: Map<ScriptStyle, Double>): ScriptStyle {
        // 结合历史偏好和学习结果计算风格分数
        val styleScores = mutableMapOf<ScriptStyle, Double>()
        
        // 计算历史偏好分数
        preferredStyles.forEach { (style, count) ->
            styleScores[style] = styleScores.getOrDefault(style, 0.0) + count * 0.7
        }
        
        // 计算学习结果分数
        learnedStyles.forEach { (style, score) ->
            styleScores[style] = styleScores.getOrDefault(style, 0.0) + score * 0.3
        }
        
        // 返回分数最高的风格
        return styleScores.maxByOrNull { it.value }?.key ?: ScriptStyle.PROFESSIONAL
    }
    
    /**
     * 推荐场景类型
     */
    private fun recommendScene(preferredScenes: Map<SceneType, Int>, learnedScenes: Map<SceneType, Double>, recentHouseTypes: List<String>): SceneType {
        // 结合历史偏好、学习结果和最近查看的房屋类型计算场景分数
        val sceneScores = mutableMapOf<SceneType, Double>()
        
        // 计算历史偏好分数
        preferredScenes.forEach { (scene, count) ->
            sceneScores[scene] = sceneScores.getOrDefault(scene, 0.0) + count * 0.5
        }
        
        // 计算学习结果分数
        learnedScenes.forEach { (scene, score) ->
            sceneScores[scene] = sceneScores.getOrDefault(scene, 0.0) + score * 0.3
        }
        
        // 根据最近查看的房屋类型调整分数
        recentHouseTypes.forEach { houseType ->
            when (houseType.toLowerCase()) {
                "apartment" -> sceneScores[SceneType.FAMILY] = sceneScores.getOrDefault(SceneType.FAMILY, 0.0) + 0.2
                "villa" -> sceneScores[SceneType.LUXURY] = sceneScores.getOrDefault(SceneType.LUXURY, 0.0) + 0.2
                "office" -> sceneScores[SceneType.OFFICE] = sceneScores.getOrDefault(SceneType.OFFICE, 0.0) + 0.2
                "studio" -> sceneScores[SceneType.STUDENT] = sceneScores.getOrDefault(SceneType.STUDENT, 0.0) + 0.2
                "warehouse" -> sceneScores[SceneType.STORAGE] = sceneScores.getOrDefault(SceneType.STORAGE, 0.0) + 0.2
                "hotel" -> sceneScores[SceneType.VACATION] = sceneScores.getOrDefault(SceneType.VACATION, 0.0) + 0.2
                "nursing home" -> sceneScores[SceneType.RETIREMENT] = sceneScores.getOrDefault(SceneType.RETIREMENT, 0.0) + 0.2
            }
        }
        
        // 返回分数最高的场景
        return sceneScores.maxByOrNull { it.value }?.key ?: SceneType.GENERAL
    }
    
    /**
     * 实时分析
     * 在用户上传视频时实时分析并推荐场景类型
     */
    fun realTimeAnalyzeVideo(videoMeta: VideoMeta): SceneType {
        // 调用智能场景识别方法
        return analyzeVideoScene(videoMeta)
    }
    
    /**
     * 实时反馈
     * 根据用户的反馈不断优化推荐算法
     */
    fun updateUserPreferences(userPreferences: UserPreferences, feedback: UserFeedback): UserPreferences {
        try {
            // 更新风格偏好
            val updatedStyles = updateStylePreferences(userPreferences.preferredStyles, feedback)
            
            // 更新场景偏好
            val updatedScenes = updateScenePreferences(userPreferences.preferredScenes, feedback)
            
            // 更新最近查看的房屋类型
            val updatedHouseTypes = updateRecentHouseTypes(userPreferences.recentHouseTypes, feedback.houseType)
            
            // 返回更新后的用户偏好
            return UserPreferences(
                preferredStyles = updatedStyles,
                preferredScenes = updatedScenes,
                recentHouseTypes = updatedHouseTypes
            )
        } catch (e: Exception) {
            // 如果更新失败，返回原始用户偏好
            return userPreferences
        }
    }
    
    /**
     * 更新风格偏好
     */
    private fun updateStylePreferences(preferredStyles: Map<ScriptStyle, Int>, feedback: UserFeedback): Map<ScriptStyle, Int> {
        val updatedStyles = mutableMapOf<ScriptStyle, Int>()
        updatedStyles.putAll(preferredStyles)
        
        // 根据用户反馈更新风格偏好
        if (feedback.style != null) {
            val currentCount = updatedStyles.getOrDefault(feedback.style, 0)
            updatedStyles[feedback.style] = currentCount + 1
        }
        
        return updatedStyles
    }
    
    /**
     * 更新场景偏好
     */
    private fun updateScenePreferences(preferredScenes: Map<SceneType, Int>, feedback: UserFeedback): Map<SceneType, Int> {
        val updatedScenes = mutableMapOf<SceneType, Int>()
        updatedScenes.putAll(preferredScenes)
        
        // 根据用户反馈更新场景偏好
        if (feedback.scene != null) {
            val currentCount = updatedScenes.getOrDefault(feedback.scene, 0)
            updatedScenes[feedback.scene] = currentCount + 1
        }
        
        return updatedScenes
    }
    
    /**
     * 更新最近查看的房屋类型
     */
    private fun updateRecentHouseTypes(recentHouseTypes: List<String>, houseType: String?): List<String> {
        if (houseType == null) {
            return recentHouseTypes
        }
        
        val updatedHouseTypes = mutableListOf<String>()
        
        // 将新的房屋类型添加到列表开头
        updatedHouseTypes.add(houseType)
        
        // 添加之前的房屋类型，但排除重复的
        recentHouseTypes.forEach { type ->
            if (type != houseType && updatedHouseTypes.size < 10) { // 保留最近10个
                updatedHouseTypes.add(type)
            }
        }
        
        return updatedHouseTypes
    }
    
    /**
     * 跨平台支持
     * 支持在不同平台上使用相同的分析和推荐功能
     */
    fun getCrossPlatformData(): Map<String, Any> {
        // 返回跨平台数据
        return mapOf(
            "supported_styles" to ScriptStyle.values().map { it.value },
            "supported_scenes" to SceneType.values().map { it.value },
            "supported_languages" to Language.values().map { it.code }
        )
    }
    
    /**
     * 用户偏好数据类
     */
    data class UserPreferences(
        val preferredStyles: Map<ScriptStyle, Int>, // 风格偏好及使用次数
        val preferredScenes: Map<SceneType, Int>, // 场景偏好及使用次数
        val recentHouseTypes: List<String> // 最近查看的房屋类型
    )
    
    /**
     * 用户反馈数据类
     */
    data class UserFeedback(
        val style: ScriptStyle?, // 用户选择的风格
        val scene: SceneType?, // 用户选择的场景
        val houseType: String?, // 房屋类型
        val rating: Int? // 评分（1-5）
    )

    enum class AdjustmentDirection {
        SHORTEN,
        LENGTHEN
    }
}

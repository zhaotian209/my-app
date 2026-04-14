package com.realestate.videopack.core.engine

import com.realestate.videopack.data.local.entity.HouseInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class CoverGeneratorTest {
    
    private lateinit var coverGenerator: CoverGenerator
    
    @Before
    fun setUp() {
        // 创建 CoverGenerator 实例
        coverGenerator = CoverGenerator()
    }
    
    @Test
    fun testGenerateCover() {
        // 测试生成封面
        val imagePath = "${System.getProperty("java.io.tmpdir")}/test_image.jpg"
        
        // 创建测试图片文件
        val testFile = File(imagePath)
        testFile.createNewFile()
        
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
        
        try {
            val coverPath = coverGenerator.generateCover(imagePath, houseInfo)
            
            // 验证结果
            Assert.assertNotNull(coverPath)
            Assert.assertTrue(coverPath.isNotEmpty())
            val coverFile = File(coverPath)
            Assert.assertTrue(coverFile.exists())
            Assert.assertTrue(coverFile.length() > 0)
        } finally {
            // 清理测试文件
            testFile.delete()
        }
    }
}

package com.realestate.videopack.core.utils

import org.junit.Assert
import org.junit.Test
import java.io.File

class UtilsTest {
    
    @Test
    fun testFileUtils() {
        // 测试文件工具类
        val testDir = System.getProperty("java.io.tmpdir")
        val testFile = File(testDir, "test_file.txt")
        
        // 创建测试文件
        testFile.createNewFile()
        testFile.writeText("Test content")
        
        // 测试文件存在性
        Assert.assertTrue(FileUtils.exists(testFile.path))
        
        // 测试文件读取
        val content = FileUtils.readFile(testFile.path)
        Assert.assertEquals("Test content", content)
        
        // 测试文件删除
        FileUtils.deleteFile(testFile.path)
        Assert.assertFalse(testFile.exists())
    }
    
    @Test
    fun testMediaUtils() {
        // 测试媒体工具类
        val testDurationMs = 15000 // 15秒
        val formattedDuration = MediaUtils.formatDuration(testDurationMs)
        Assert.assertEquals("00:15", formattedDuration)
        
        val testFileSize = 1024 * 1024 // 1MB
        val formattedSize = MediaUtils.formatFileSize(testFileSize)
        Assert.assertEquals("1.0 MB", formattedSize)
    }
    
    @Test
    fun testSrtSubtitleUtils() {
        // 测试SRT字幕工具类
        val testScript = "阳光小区，3室2厅，120平米，售价88万，精装修。"
        val subtitleContent = SrtSubtitleUtils.generateSrt(testScript)
        Assert.assertNotNull(subtitleContent)
        Assert.assertTrue(subtitleContent.isNotEmpty())
    }
    
    @Test
    fun testAssSubtitleUtils() {
        // 测试ASS字幕工具类
        val testScript = "阳光小区，3室2厅，120平米，售价88万，精装修。"
        val subtitleContent = AssSubtitleUtils.generateAss(testScript)
        Assert.assertNotNull(subtitleContent)
        Assert.assertTrue(subtitleContent.isNotEmpty())
    }
    
    @Test
    fun testFFmpegUtils() {
        // 测试FFmpeg工具类
        val testCommand = "-i input.mp4 -c copy output.mp4"
        val escapedCommand = FFmpegUtils.escapeCommand(testCommand)
        Assert.assertNotNull(escapedCommand)
        Assert.assertTrue(escapedCommand.isNotEmpty())
    }
    
    @Test
    fun testExcelUtils() {
        // 测试Excel工具类
        val testData = listOf(
            listOf("小区", "户型", "面积", "价格"),
            listOf("阳光小区", "3室2厅", "120", "88万"),
            listOf("幸福小区", "2室1厅", "80", "55万")
        )
        
        val outputPath = "${System.getProperty("java.io.tmpdir")}/test_excel.xlsx"
        val success = ExcelUtils.generateExcel(testData, outputPath)
        Assert.assertTrue(success)
        
        // 验证文件存在
        val excelFile = File(outputPath)
        Assert.assertTrue(excelFile.exists())
        
        // 清理测试文件
        excelFile.delete()
    }
    
    @Test
    fun testPermissionUtils() {
        // 测试权限工具类
        val requiredPermissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        
        // 测试权限数组转换为字符串
        val permissionString = PermissionUtils.permissionsToString(requiredPermissions)
        Assert.assertNotNull(permissionString)
        Assert.assertTrue(permissionString.isNotEmpty())
    }
}

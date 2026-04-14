package com.realestate.videopack.core.engine

import android.graphics.*
import android.graphics.fonts.FontFamily
import android.graphics.fonts.FontStyle
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import com.realestate.videopack.data.local.entity.HouseInfo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoverGenerator @Inject constructor() {
    
    /**
     * 生成统一风格的封面
     * @param imagePath 用户上传的图片路径
     * @param houseInfo 房屋信息
     * @return 生成的封面图片路径
     */
    fun generateCover(imagePath: String, houseInfo: HouseInfo): String {
        try {
            // 1. 读取上传的图片
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap == null) {
                throw IOException("Failed to decode image")
            }
            
            // 2. 创建新的 bitmap 用于绘制封面
            val coverBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(coverBitmap)
            
            // 3. 添加半透明背景
            addBackgroundOverlay(canvas, coverBitmap.width, coverBitmap.height)
            
            // 4. 排版房屋信息
            layoutHouseInfo(canvas, coverBitmap.width, coverBitmap.height, houseInfo)
            
            // 5. 保存封面图片
            val outputPath = "${File(imagePath).parent}/cover_${System.currentTimeMillis()}.jpg"
            saveBitmap(coverBitmap, outputPath)
            
            // 6. 释放资源
            bitmap.recycle()
            coverBitmap.recycle()
            
            return outputPath
        } catch (e: Exception) {
            throw RuntimeException("Failed to generate cover: ${e.message}", e)
        }
    }
    
    /**
     * 添加半透明背景
     */
    private fun addBackgroundOverlay(canvas: Canvas, width: Int, height: Int) {
        val paint = Paint()
        paint.color = Color.argb(150, 0, 0, 0) // 半透明黑色
        canvas.drawRect(0f, height * 0.7f, width.toFloat(), height.toFloat(), paint)
    }
    
    /**
     * 排版房屋信息
     */
    private fun layoutHouseInfo(canvas: Canvas, width: Int, height: Int, houseInfo: HouseInfo) {
        val margin = 40
        var y = height * 0.7f + margin
        
        // 社区名称
        drawText(canvas, houseInfo.community, width, y.toInt(), 36f, Color.WHITE, true)
        y += 50
        
        // 户型和面积
        val layoutText = "${houseInfo.layout} · ${houseInfo.area}平米"
        drawText(canvas, layoutText, width, y.toInt(), 24f, Color.WHITE, false)
        y += 40
        
        // 价格
        drawText(canvas, houseInfo.price, width, y.toInt(), 32f, Color.YELLOW, true)
        y += 40
        
        // 装修情况
        if (houseInfo.decoration.isNotEmpty()) {
            drawText(canvas, houseInfo.decoration + "装修", width, y.toInt(), 20f, Color.WHITE, false)
            y += 35
        }
        
        // 卖点
        if (houseInfo.sellingPoints.isNotEmpty()) {
            val sellingPointsText = houseInfo.sellingPoints.joinToString(" · ")
            drawMultilineText(canvas, sellingPointsText, width, y.toInt(), 18f, Color.WHITE, margin)
        }
    }
    
    /**
     * 绘制单行文本
     */
    private fun drawText(canvas: Canvas, text: String, width: Int, y: Int, textSize: Float, color: Int, isBold: Boolean) {
        val paint = TextPaint()
        paint.color = color
        paint.textSize = textSize
        paint.isAntiAlias = true
        if (isBold) {
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        
        val textWidth = paint.measureText(text)
        val x = (width - textWidth) / 2
        canvas.drawText(text, x, y.toFloat(), paint)
    }
    
    /**
     * 绘制多行文本
     */
    private fun drawMultilineText(canvas: Canvas, text: String, width: Int, y: Int, textSize: Float, color: Int, margin: Int) {
        val paint = TextPaint()
        paint.color = color
        paint.textSize = textSize
        paint.isAntiAlias = true
        
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width - margin * 2)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(text, paint, width - margin * 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
        }
        
        canvas.save()
        canvas.translate(margin.toFloat(), y.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }
    
    /**
     * 保存 bitmap 到文件
     */
    private fun saveBitmap(bitmap: Bitmap, outputPath: String) {
        val file = File(outputPath)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
    }
}
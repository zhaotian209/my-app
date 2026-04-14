package com.realestate.videopack.core.utils

import com.realestate.videopack.data.local.entity.HouseInfo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ExcelUtils {
    fun exportHouseInfoToExcel(houseInfoList: List<HouseInfo>, outputPath: String): Boolean {
        try {
            val file = File(outputPath)
            val fos = FileOutputStream(file)
            
            // 写入CSV格式数据
            val csvContent = StringBuilder()
            // 写入表头
            csvContent.append("小区,户型,面积,价格,装修,卖点,楼层,朝向,建造年份,电梯,地铁,学校,物业类型,看房方式,创建时间\n")
            
            // 写入数据
            houseInfoList.forEach { house ->
                csvContent.append("${house.community},")
                csvContent.append("${house.layout},")
                csvContent.append("${house.area},")
                csvContent.append("${house.price},")
                csvContent.append("${house.decoration},")
                csvContent.append("${house.sellingPoints.joinToString("|")},")
                csvContent.append("${house.floor ?: ""},")
                csvContent.append("${house.orientation ?: ""},")
                csvContent.append("${house.buildYear ?: ""},")
                csvContent.append("${house.elevator ?: ""},")
                csvContent.append("${house.metro ?: ""},")
                csvContent.append("${house.school ?: ""},")
                csvContent.append("${house.propertyType ?: ""},")
                csvContent.append("${house.visitType ?: ""},")
                csvContent.append("${house.createTime}\n")
            }
            
            fos.write(csvContent.toString().toByteArray())
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
}
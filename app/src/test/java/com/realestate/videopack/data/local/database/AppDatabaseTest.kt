package com.realestate.videopack.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.realestate.videopack.data.local.entity.HouseInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AppDatabaseTest {
    
    private lateinit var database: AppDatabase
    
    @Before
    fun setUp() {
        // 创建内存数据库
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
    }
    
    @After
    fun tearDown() {
        database.close()
    }
    
    @Test
    fun testInsertAndGetHouseInfo() = runBlocking {
        // 创建测试数据
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
        
        // 插入数据
        database.houseInfoDao().insert(houseInfo)
        
        // 获取数据
        val insertedHouseInfo = database.houseInfoDao().getAll()[0]
        
        // 验证结果
        Assert.assertNotNull(insertedHouseInfo)
        Assert.assertEquals(houseInfo.community, insertedHouseInfo.community)
        Assert.assertEquals(houseInfo.layout, insertedHouseInfo.layout)
        Assert.assertEquals(houseInfo.area, insertedHouseInfo.area)
        Assert.assertEquals(houseInfo.price, insertedHouseInfo.price)
    }
    
    @Test
    fun testDeleteHouseInfo() = runBlocking {
        // 创建测试数据
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
        
        // 插入数据
        database.houseInfoDao().insert(houseInfo)
        Assert.assertEquals(1, database.houseInfoDao().getAll().size)
        
        // 删除数据
        database.houseInfoDao().delete(houseInfo)
        
        // 验证结果
        Assert.assertEquals(0, database.houseInfoDao().getAll().size)
    }
}

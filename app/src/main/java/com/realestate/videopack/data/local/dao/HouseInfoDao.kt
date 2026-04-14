package com.realestate.videopack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.realestate.videopack.data.local.entity.HouseInfo

@Dao
interface HouseInfoDao {
    @Query("SELECT * FROM house_info ORDER BY createTime DESC")
    suspend fun getAll(): List<HouseInfo>

    @Insert
    suspend fun insert(house: HouseInfo): Long

    @Delete
    suspend fun delete(house: HouseInfo)
}
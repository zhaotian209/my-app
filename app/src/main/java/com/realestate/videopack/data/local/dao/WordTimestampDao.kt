package com.realestate.videopack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.realestate.videopack.data.local.entity.WordTimestamp

@Dao
interface WordTimestampDao {
    @Query("SELECT * FROM word_timestamp WHERE houseId=:houseId")
    suspend fun getByHouseId(houseId:Long): List<WordTimestamp>

    @Insert
    suspend fun insertAll(timestamps: List<WordTimestamp>)
}
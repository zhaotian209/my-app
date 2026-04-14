package com.realestate.videopack.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.realestate.videopack.data.local.entity.Template

@Dao
interface TemplateDao {
    @Query("SELECT * FROM template")
    suspend fun getAll(): List<Template>
}
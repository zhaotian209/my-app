package com.realestate.videopack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="template")
data class Template(
    @PrimaryKey(autoGenerate=true)
    val id:Long=0,
    val name:String,
    val voice:String,
    val bgmRes:Int,
    val filterPreset:String
)
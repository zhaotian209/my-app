package com.realestate.videopack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="house_info")
data class HouseInfo(
    @PrimaryKey(autoGenerate=true)
    val id:Long=0,
    val community:String,
    val layout:String,
    val area:String,
    val price:String,
    val decoration:String,
    val sellingPoints:List<String>,
    val floor:String?=null,
    val orientation:String?=null,
    val buildYear:String?=null,
    val elevator:Boolean?=null,
    val metro:String?=null,
    val school:String?=null,
    val propertyType:String?=null,
    val visitType:String?=null,
    val createTime:Long=System.currentTimeMillis()
)
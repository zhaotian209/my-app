package com.realestate.videopack.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="word_timestamp", foreignKeys=[
    ForeignKey(
        entity=HouseInfo::class,
        parentColumns=["id"],
        childColumns=["houseId"],
        onDelete=ForeignKey.CASCADE
    )
])
data class WordTimestamp(
    @PrimaryKey(autoGenerate=true)
    val id:Long=0,
    val houseId:Long,
    val text:String,
    val startMs:Int,
    val endMs:Int
)
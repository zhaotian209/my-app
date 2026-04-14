package com.realestate.videopack.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.data.local.entity.Template
import com.realestate.videopack.data.local.entity.WordTimestamp
import com.realestate.videopack.data.local.dao.HouseInfoDao
import com.realestate.videopack.data.local.dao.TemplateDao
import com.realestate.videopack.data.local.dao.WordTimestampDao

@Database(entities=[HouseInfo::class, WordTimestamp::class, Template::class], version=1, exportSchema=true)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun houseInfoDao(): HouseInfoDao
    abstract fun wordTimestampDao(): WordTimestampDao
    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE:AppDatabase?=null

        fun getInstance(context:Context):AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "real_estate_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE=it }
            }
        }
    }
}

class ListConverter {
    @androidx.room.TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @androidx.room.TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",").filter { it.isNotEmpty() }
    }
}
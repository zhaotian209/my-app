package com.realestate.videopack.domain.repository

import com.realestate.videopack.data.local.entity.HouseInfo

interface HouseInfoRepository {
    suspend fun saveHouseInfo(info:HouseInfo):Long
    suspend fun getHouseInfoList():List<HouseInfo>
}
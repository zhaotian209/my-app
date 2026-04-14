package com.realestate.videopack.data.repository

import com.realestate.videopack.data.local.dao.HouseInfoDao
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.repository.HouseInfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HouseInfoRepositoryImpl @Inject constructor(private val dao: HouseInfoDao) : HouseInfoRepository {
    override suspend fun saveHouseInfo(info: HouseInfo): Long {
        return dao.insert(info)
    }
    
    override suspend fun getHouseInfoList(): List<HouseInfo> {
        return dao.getAll()
    }
}
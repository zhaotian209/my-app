package com.realestate.videopack.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.domain.repository.HouseInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val houseInfoRepository: HouseInfoRepository) : ViewModel() {
    private val _houseInfoList = MutableStateFlow<List<HouseInfo>>(emptyList())
    val houseInfoList: StateFlow<List<HouseInfo>> = _houseInfoList.asStateFlow()

    suspend fun loadHouseInfoList() {
        val result = houseInfoRepository.getHouseInfoList()
        _houseInfoList.value = result
    }
}
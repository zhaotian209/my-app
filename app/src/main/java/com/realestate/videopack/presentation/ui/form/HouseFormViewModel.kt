package com.realestate.videopack.presentation.ui.form

import androidx.lifecycle.ViewModel
import com.realestate.videopack.core.engine.AutoVideoProcessor
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.data.local.entity.Template
import com.realestate.videopack.domain.repository.HouseInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HouseFormViewModel @Inject constructor(
    private val houseInfoRepository: HouseInfoRepository,
    val autoVideoProcessor: AutoVideoProcessor
) : ViewModel() {
    private val _houseInfo = MutableStateFlow(HouseInfo(
        community = "",
        layout = "",
        area = "",
        price = "",
        decoration = "",
        sellingPoints = emptyList()
    ))
    val houseInfo: StateFlow<HouseInfo> = _houseInfo.asStateFlow()

    private val _selectedTemplate = MutableStateFlow<Template?>(null)
    val selectedTemplate: StateFlow<Template?> = _selectedTemplate.asStateFlow()

    private val _selectedVoice = MutableStateFlow("默认")
    val selectedVoice: StateFlow<String> = _selectedVoice.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun updateCommunity(community: String) {
        _houseInfo.value = _houseInfo.value.copy(community = community)
    }

    fun updateLayout(layout: String) {
        _houseInfo.value = _houseInfo.value.copy(layout = layout)
    }

    fun updateArea(area: String) {
        _houseInfo.value = _houseInfo.value.copy(area = area)
    }

    fun updatePrice(price: String) {
        _houseInfo.value = _houseInfo.value.copy(price = price)
    }

    fun updateDecoration(decoration: String) {
        _houseInfo.value = _houseInfo.value.copy(decoration = decoration)
    }

    fun updateSellingPoints(sellingPoints: List<String>) {
        _houseInfo.value = _houseInfo.value.copy(sellingPoints = sellingPoints)
    }

    fun selectTemplate(template: Template) {
        _selectedTemplate.value = template
    }

    fun selectVoice(voice: String) {
        _selectedVoice.value = voice
    }

    suspend fun saveHouseInfo(): Long {
        _isSaving.value = true
        val result = houseInfoRepository.saveHouseInfo(_houseInfo.value)
        _isSaving.value = false
        return result
    }
}
package com.realestate.videopack.presentation.ui.preview

import androidx.lifecycle.ViewModel
import com.realestate.videopack.data.local.entity.HouseInfo
import com.realestate.videopack.data.local.entity.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor() : ViewModel() {
    private val _houseInfo = MutableStateFlow<HouseInfo?>(null)
    val houseInfo: StateFlow<HouseInfo?> = _houseInfo.asStateFlow()

    private val _template = MutableStateFlow<Template?>(null)
    val template: StateFlow<Template?> = _template.asStateFlow()

    private val _videoPath = MutableStateFlow("")
    val videoPath: StateFlow<String> = _videoPath.asStateFlow()

    private val _script = MutableStateFlow("")
    val script: StateFlow<String> = _script.asStateFlow()

    private val _showSubtitle = MutableStateFlow(true)
    val showSubtitle: StateFlow<Boolean> = _showSubtitle.asStateFlow()

    private val _showBgm = MutableStateFlow(true)
    val showBgm: StateFlow<Boolean> = _showBgm.asStateFlow()

    private val _enhanceQuality = MutableStateFlow(true)
    val enhanceQuality: StateFlow<Boolean> = _enhanceQuality.asStateFlow()

    fun setHouseInfo(info: HouseInfo) {
        _houseInfo.value = info
    }

    fun setTemplate(template: Template) {
        _template.value = template
    }

    fun setVideoPath(path: String) {
        _videoPath.value = path
    }

    fun setScript(script: String) {
        _script.value = script
    }

    fun toggleSubtitle() {
        _showSubtitle.value = !_showSubtitle.value
    }

    fun toggleBgm() {
        _showBgm.value = !_showBgm.value
    }

    fun toggleEnhanceQuality() {
        _enhanceQuality.value = !_enhanceQuality.value
    }

    fun exportVideo() {
        // 导出视频逻辑
    }
}
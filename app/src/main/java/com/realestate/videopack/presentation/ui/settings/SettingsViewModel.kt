package com.realestate.videopack.presentation.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _videoQuality = MutableStateFlow("高清 (1080p)")
    val videoQuality: StateFlow<String> = _videoQuality.asStateFlow()

    private val _saveLocation = MutableStateFlow("内部存储 / 视频")
    val saveLocation: StateFlow<String> = _saveLocation.asStateFlow()

    private val _bgmVolume = MutableStateFlow(50)
    val bgmVolume: StateFlow<Int> = _bgmVolume.asStateFlow()

    private val _subtitleSize = MutableStateFlow(16)
    val subtitleSize: StateFlow<Int> = _subtitleSize.asStateFlow()

    fun setVideoQuality(quality: String) {
        _videoQuality.value = quality
    }

    fun setSaveLocation(location: String) {
        _saveLocation.value = location
    }

    fun setBgmVolume(volume: Int) {
        _bgmVolume.value = volume
    }

    fun setSubtitleSize(size: Int) {
        _subtitleSize.value = size
    }

    fun clearCache() {
        // 清理缓存逻辑
    }

    fun exportData() {
        // 导出数据逻辑
    }
}
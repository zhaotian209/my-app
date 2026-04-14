package com.realestate.videopack.presentation.ui.upload

import androidx.lifecycle.ViewModel
import com.realestate.videopack.domain.model.VideoMeta
import com.realestate.videopack.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    private val _videoMeta = MutableStateFlow<VideoMeta?>(null)
    val videoMeta: StateFlow<VideoMeta?> = _videoMeta.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun setVideoMeta(meta: VideoMeta) {
        _videoMeta.value = meta
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}
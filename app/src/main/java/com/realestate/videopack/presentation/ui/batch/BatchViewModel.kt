package com.realestate.videopack.presentation.ui.batch

import androidx.lifecycle.ViewModel
import com.realestate.videopack.domain.model.RenderTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BatchViewModel @Inject constructor() : ViewModel() {
    private val _tasks = MutableStateFlow<List<RenderTask>>(emptyList())
    val tasks: StateFlow<List<RenderTask>> = _tasks.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    fun addTask(task: RenderTask) {
        _tasks.value = _tasks.value + task
    }

    fun removeTask(task: RenderTask) {
        _tasks.value = _tasks.value.filter { it != task }
    }

    fun clearTasks() {
        _tasks.value = emptyList()
    }

    fun startProcessing() {
        _isProcessing.value = true
        _progress.value = 0
    }

    fun updateProgress(progress: Int) {
        _progress.value = progress
    }

    fun finishProcessing() {
        _isProcessing.value = false
        _progress.value = 100
    }
}
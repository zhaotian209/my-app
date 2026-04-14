package com.realestate.videopack.core.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.realestate.videopack.domain.model.RenderTask
import com.realestate.videopack.domain.repository.VideoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class VideoRenderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: VideoRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            val houseId = inputData.getLong("houseId", 0)
            val videoPath = inputData.getString("videoPath") ?: return Result.failure()
            val templateId = inputData.getLong("templateId", 0)
            val voice = inputData.getString("voice") ?: ""

            val task = RenderTask(houseId, videoPath, templateId, voice)
            val result = repo.renderVideo(task)

            return if (result.isSuccess) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
package com.realestate.videopack.domain.usecase

import com.realestate.videopack.domain.model.RenderTask
import com.realestate.videopack.domain.repository.VideoRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BatchProcessUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    suspend operator fun invoke(tasks: List<RenderTask>): List<Result<String>> {
        return withContext(Dispatchers.IO) {
            tasks.map { task ->
                videoRepository.renderVideo(task)
            }
        }
    }
}
package com.realestate.videopack.domain.usecase

import com.realestate.videopack.domain.model.RenderTask
import com.realestate.videopack.domain.repository.VideoRepository
import javax.inject.Inject

class ProcessVideoUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    suspend operator fun invoke(task: RenderTask): Result<String> {
        return videoRepository.renderVideo(task)
    }
}
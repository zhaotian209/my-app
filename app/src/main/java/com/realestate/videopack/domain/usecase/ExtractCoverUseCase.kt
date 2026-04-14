package com.realestate.videopack.domain.usecase

import com.realestate.videopack.domain.repository.VideoRepository
import javax.inject.Inject
import java.io.File

class ExtractCoverUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    suspend operator fun invoke(videoPath: String, outputPath: String): Result<File> {
        return videoRepository.extractCover(videoPath, outputPath)
    }
}
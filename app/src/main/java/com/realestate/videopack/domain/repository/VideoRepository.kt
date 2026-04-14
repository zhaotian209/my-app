package com.realestate.videopack.domain.repository

import com.realestate.videopack.domain.model.RenderTask
import java.io.File

interface VideoRepository {
    suspend fun extractCover(videoPath:String, outputPath:String):Result<File>
    suspend fun renderVideo(task:RenderTask):Result<String>
}
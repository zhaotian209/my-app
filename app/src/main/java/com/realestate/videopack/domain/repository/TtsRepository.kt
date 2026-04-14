package com.realestate.videopack.domain.repository

import com.realestate.videopack.domain.model.TtsWord
import java.io.File

interface TtsRepository {
    suspend fun synthesize(text:String, voice:String, outputPath:String):Result<Pair<List<TtsWord>, File>>
}
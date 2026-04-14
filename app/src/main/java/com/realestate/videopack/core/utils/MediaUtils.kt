package com.realestate.videopack.core.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.realestate.videopack.domain.model.VideoMeta

object MediaUtils {
    fun getVideoMeta(context: Context, videoUri: Uri): VideoMeta? {
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT
        )

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(videoUri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val durationMs = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                val width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
                val height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
                return VideoMeta(
                    path = videoUri.path ?: "",
                    durationMs = durationMs,
                    width = width,
                    height = height,
                    fps = 30.0f // 默认帧率
                )
            }
        } catch (e: Exception) {
            Log.e("MediaUtils", "Error getting video meta", e)
        } finally {
            cursor?.close()
        }
        return null
    }

    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
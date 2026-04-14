package com.realestate.videopack.core.service

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestWorkerBuilder
import com.realestate.videopack.domain.repository.VideoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class VideoRenderWorkerTest {
    
    private lateinit var mockContext: Context
    private lateinit var mockRepository: VideoRepository
    
    @Before
    fun setUp() {
        // 模拟 Context
        mockContext = Mockito.mock(Context::class.java)
        
        // 模拟 VideoRepository
        mockRepository = Mockito.mock(VideoRepository::class.java)
        
        // 模拟 renderVideo 方法返回成功
        `when`(mockRepository.renderVideo(Mockito.any())).thenReturn(Result.success("/tmp/output.mp4"))
    }
    
    @Test
    fun testDoWork() = runBlocking {
        // 创建输入数据
        val inputData = Data.Builder()
            .putLong("houseId", 1)
            .putString("videoPath", "/tmp/video.mp4")
            .putLong("templateId", 1)
            .putString("voice", "system")
            .build()
        
        // 创建测试工作器
        val worker = TestWorkerBuilder<VideoRenderWorker>(mockContext)
            .setInputData(inputData)
            .setWorkerFactory(VideoRenderWorkerFactory(mockRepository))
            .build()
        
        // 执行工作
        val result = worker.doWork()
        
        // 验证结果
        Assert.assertEquals(ListenableWorker.Result.success(), result)
    }
    
    @Test
    fun testDoWorkWithFailure() = runBlocking {
        // 模拟 renderVideo 方法返回失败
        `when`(mockRepository.renderVideo(Mockito.any())).thenReturn(Result.failure(Exception("Render failed")))
        
        // 创建输入数据
        val inputData = Data.Builder()
            .putLong("houseId", 1)
            .putString("videoPath", "/tmp/video.mp4")
            .putLong("templateId", 1)
            .putString("voice", "system")
            .build()
        
        // 创建测试工作器
        val worker = TestWorkerBuilder<VideoRenderWorker>(mockContext)
            .setInputData(inputData)
            .setWorkerFactory(VideoRenderWorkerFactory(mockRepository))
            .build()
        
        // 执行工作
        val result = worker.doWork()
        
        // 验证结果
        Assert.assertEquals(ListenableWorker.Result.failure(), result)
    }
    
    // 测试用的 WorkerFactory
    class VideoRenderWorkerFactory(private val repository: VideoRepository) : androidx.work.WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: androidx.work.WorkerParameters
        ): ListenableWorker? {
            return VideoRenderWorker(appContext, workerParameters, repository)
        }
    }
}

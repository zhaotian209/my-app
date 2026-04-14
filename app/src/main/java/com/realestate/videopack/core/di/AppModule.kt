package com.realestate.videopack.core.di

import android.content.Context
import androidx.room.Room
import com.realestate.videopack.core.engine.AiScriptGenerator
import com.realestate.videopack.core.engine.AutoVideoProcessor
import com.realestate.videopack.core.engine.ComplianceChecker
import com.realestate.videopack.core.engine.CoverGenerator
import com.realestate.videopack.core.engine.TtsManager
import com.realestate.videopack.core.engine.VideoProcessingEngine
import com.realestate.videopack.data.local.database.AppDatabase
import com.realestate.videopack.data.local.dao.HouseInfoDao
import com.realestate.videopack.data.local.dao.TemplateDao
import com.realestate.videopack.data.local.dao.WordTimestampDao
import com.realestate.videopack.data.repository.HouseInfoRepositoryImpl
import com.realestate.videopack.data.repository.TtsRepositoryImpl
import com.realestate.videopack.data.repository.VideoRepositoryImpl
import com.realestate.videopack.domain.repository.HouseInfoRepository
import com.realestate.videopack.domain.repository.TtsRepository
import com.realestate.videopack.domain.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "real_estate_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHouseInfoDao(db: AppDatabase): HouseInfoDao {
        return db.houseInfoDao()
    }

    @Provides
    fun provideWordTimestampDao(db: AppDatabase): WordTimestampDao {
        return db.wordTimestampDao()
    }

    @Provides
    fun provideTemplateDao(db: AppDatabase): TemplateDao {
        return db.templateDao()
    }

    @Provides
    fun provideTtsManager(@ApplicationContext context: Context): TtsManager {
        return TtsManager(context)
    }

    @Provides
    fun provideVideoProcessingEngine(): VideoProcessingEngine {
        return VideoProcessingEngine()
    }

    @Provides
    fun provideComplianceChecker(): ComplianceChecker {
        return ComplianceChecker()
    }

    @Provides
    @Singleton
    fun provideAiScriptGenerator(): AiScriptGenerator {
        return AiScriptGenerator()
    }

    @Provides
    @Singleton
    fun provideCoverGenerator(): CoverGenerator {
        return CoverGenerator()
    }

    @Provides
    @Singleton
    fun provideAutoVideoProcessor(
        aiScriptGenerator: AiScriptGenerator,
        ttsManager: TtsManager,
        videoProcessingEngine: VideoProcessingEngine,
        complianceChecker: ComplianceChecker,
        coverGenerator: CoverGenerator
    ): AutoVideoProcessor {
        return AutoVideoProcessor(
            aiScriptGenerator = aiScriptGenerator,
            ttsManager = ttsManager,
            videoProcessingEngine = videoProcessingEngine,
            complianceChecker = complianceChecker,
            coverGenerator = coverGenerator
        )
    }

    @Provides
    @Singleton
    fun provideHouseInfoRepository(dao: HouseInfoDao): HouseInfoRepository {
        return HouseInfoRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTtsRepository(): TtsRepository {
        return TtsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideVideoRepository(): VideoRepository {
        return VideoRepositoryImpl()
    }
}
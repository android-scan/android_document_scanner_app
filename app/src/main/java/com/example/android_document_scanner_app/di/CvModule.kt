package com.example.android_document_scanner_app.di

import android.content.Context
import com.example.android_document_scanner_app.cv.CornerDetector
import com.example.android_document_scanner_app.cv.ImageProcessor
import com.example.android_document_scanner_app.cv.OnnxCornerDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CvModule {

    @Provides
    @Singleton
    fun provideCornerDetector(@ApplicationContext context: Context): CornerDetector =
        OnnxCornerDetector(context)

    @Provides
    @Singleton
    fun provideImageProcessor(): ImageProcessor = ImageProcessor()
}

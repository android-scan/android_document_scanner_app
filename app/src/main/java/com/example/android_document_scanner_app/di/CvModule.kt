package com.example.android_document_scanner_app.di

import com.example.android_document_scanner_app.cv.CornerDetector
import com.example.android_document_scanner_app.cv.ImageProcessor
import com.example.android_document_scanner_app.cv.OpenCVCornerDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CvModule {

    @Provides
    @Singleton
    fun provideCornerDetector(): CornerDetector {
        // ДС ЧАСТЬ: заменить на OnnxCornerDetector(context) когда получим model.onnx
        return OpenCVCornerDetector()
    }

    @Provides
    @Singleton
    fun provideImageProcessor(): ImageProcessor = ImageProcessor()
}

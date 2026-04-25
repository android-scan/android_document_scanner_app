package com.example.android_document_scanner_app.cv

import android.graphics.Bitmap
import android.graphics.PointF

interface CornerDetector {
    // Всегда возвращает ровно 4 точки: TL, TR, BR, BL
    fun detect(bitmap: Bitmap): List<PointF>
}

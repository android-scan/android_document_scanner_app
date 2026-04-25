package com.example.android_document_scanner_app.cv

import android.graphics.Bitmap
import android.graphics.PointF

// Заглушка: возвращает углы по границам bitmap.
// ДС ЧАСТЬ: заменить на Canny + findContours через OpenCV когда подключим нативную либу.
class OpenCVCornerDetector : CornerDetector {

    override fun detect(bitmap: Bitmap): List<PointF> {
        val w = bitmap.width.toFloat()
        val h = bitmap.height.toFloat()
        val margin = minOf(w, h) * 0.05f
        return listOf(
            PointF(margin, margin),         // TL
            PointF(w - margin, margin),     // TR
            PointF(w - margin, h - margin), // BR
            PointF(margin, h - margin),     // BL
        )
    }
}

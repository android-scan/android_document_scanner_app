package com.example.android_document_scanner_app.cv

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF

class ImageProcessor {

    // ДС ЧАСТЬ: заменить на OpenCV warpPerspective когда подключим нативную либу
    fun warpPerspective(bitmap: Bitmap, corners: List<PointF>): Bitmap = bitmap

    // Фильтры: AUTO / BW / GRAY / ORIGINAL
    // ДС ЧАСТЬ: реализовать через OpenCV cvtColor + equalizeHist
    fun applyFilter(bitmap: Bitmap, filter: String): Bitmap = bitmap

    fun rotate90(bitmap: Bitmap): Bitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,
            Matrix().apply { postRotate(90f) }, true)

    fun rotate90Ccw(bitmap: Bitmap): Bitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,
            Matrix().apply { postRotate(-90f) }, true)
}

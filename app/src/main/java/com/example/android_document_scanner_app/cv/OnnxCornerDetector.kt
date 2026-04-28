package com.example.android_document_scanner_app.cv

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF

class OnnxCornerDetector(
    private val context: Context,
) : CornerDetector {

    override fun detect(bitmap: Bitmap): List<PointF> {
        //TODO: добавить нормализацию входа, работа для ДС
        ensureModelAssetExists()
        error(
            "нужна нормализация входа для $MODEL_ASSET_NAME"
        )
    }

    private fun ensureModelAssetExists() {
        context.assets.open(MODEL_ASSET_NAME).use { stream ->
            check(stream.read() != -1) { "$MODEL_ASSET_NAME пустой" }
        }
    }

    private companion object {
        const val MODEL_ASSET_NAME = "model.onnx"
        const val INPUT_NAME = "input"
        const val OUTPUT_NAME = "corners"
        const val INPUT_SIZE = 640
        const val OUTPUT_COORDS = 8
    }
}

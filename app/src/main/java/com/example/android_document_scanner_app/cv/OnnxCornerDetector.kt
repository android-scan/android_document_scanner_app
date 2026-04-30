package com.example.android_document_scanner_app.cv

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.microsoft.onnxruntime.OnnxTensor
import com.microsoft.onnxruntime.OnnxValue
import com.microsoft.onnxruntime.OrtEnvironment
import com.microsoft.onnxruntime.OrtSession
import java.nio.FloatBuffer

class OnnxCornerDetector(
    private val context: Context,
) : CornerDetector {

    private val environment: OrtEnvironment by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        OrtEnvironment.getEnvironment()
    }

    private val session: OrtSession by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        environment.createSession(readModelBytes(), OrtSession.SessionOptions()).also { createdSession ->
            check(createdSession.inputNames.contains(INPUT_NAME)) {
                "В ONNX модели нет входа $INPUT_NAME"
            }
            check(createdSession.outputNames.contains(OUTPUT_NAME)) {
                "В ONNX модели нет выхода $OUTPUT_NAME"
            }
        }
    }

    override fun detect(bitmap: Bitmap): List<PointF> {
        val input = preprocess(bitmap)
        val shape = longArrayOf(1L, CHANNELS.toLong(), INPUT_SIZE.toLong(), INPUT_SIZE.toLong())

        OnnxTensor.createTensor(environment, FloatBuffer.wrap(input), shape).use { tensor ->
            session.run(mapOf(INPUT_NAME to tensor)).use { result ->
                val output = result.get(OUTPUT_NAME).orElseThrow {
                    IllegalStateException("ONNX модель не вернула выход $OUTPUT_NAME")
                }
                val values = output.toFloatArray()
                check(values.size == OUTPUT_COORDS) {
                    "ONNX модель вернула ${values.size} координат вместо $OUTPUT_COORDS"
                }
                return values.toPoints(bitmap.width, bitmap.height)
            }
        }
    }

    private fun readModelBytes(): ByteArray =
        context.assets.open(MODEL_ASSET_NAME).use { stream ->
            stream.readBytes().also { bytes ->
                check(bytes.isNotEmpty()) { "$MODEL_ASSET_NAME пустой" }
            }
        }

    private fun preprocess(bitmap: Bitmap): FloatArray {
        val scaled = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        scaled.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)
        if (scaled !== bitmap) scaled.recycle()

        val area = INPUT_SIZE * INPUT_SIZE
        val input = FloatArray(CHANNELS * area)

        pixels.forEachIndexed { index, color ->
            val r = ((color shr 16) and 0xFF) / MAX_PIXEL_VALUE
            val g = ((color shr 8) and 0xFF) / MAX_PIXEL_VALUE
            val b = (color and 0xFF) / MAX_PIXEL_VALUE

            input[index] = (r - MEAN_R) / STD_R
            input[area + index] = (g - MEAN_G) / STD_G
            input[area * 2 + index] = (b - MEAN_B) / STD_B
        }

        return input
    }

    private fun OnnxValue.toFloatArray(): FloatArray {
        val raw = value
        return when (raw) {
            is FloatArray -> raw
            is Array<*> -> {
                val firstBatch = raw.firstOrNull()
                    ?: error("ONNX модель вернула пустой выход $OUTPUT_NAME")
                when (firstBatch) {
                    is FloatArray -> firstBatch
                    else -> error("Неподдерживаемый формат выхода ONNX: ${raw::class.java.name}")
                }
            }

            else -> error("Неподдерживаемый формат выхода ONNX: ${raw::class.java.name}")
        }
    }

    private fun FloatArray.toPoints(width: Int, height: Int): List<PointF> {
        val scaleX = maxOf(width - 1, 1).toFloat()
        val scaleY = maxOf(height - 1, 1).toFloat()

        return List(POINT_COUNT) { index ->
            val x = this[index * 2].coerceIn(0f, 1f) * scaleX
            val y = this[index * 2 + 1].coerceIn(0f, 1f) * scaleY
            PointF(x, y)
        }
    }

    private companion object {
        const val MODEL_ASSET_NAME = "model.onnx"
        const val INPUT_NAME = "input"
        const val OUTPUT_NAME = "corners"
        const val INPUT_SIZE = 640
        const val CHANNELS = 3
        const val POINT_COUNT = 4
        const val OUTPUT_COORDS = 8
        const val MAX_PIXEL_VALUE = 255f

        const val MEAN_R = 0.485f
        const val MEAN_G = 0.456f
        const val MEAN_B = 0.406f

        const val STD_R = 0.229f
        const val STD_G = 0.224f
        const val STD_B = 0.225f
    }
}

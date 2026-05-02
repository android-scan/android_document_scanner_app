package com.example.android_document_scanner_app.cv

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sqrt
import org.opencv.core.Point as CvPoint

sealed class DocumentAspectRatio {
    data object Auto : DocumentAspectRatio()
    data object A4Portrait : DocumentAspectRatio()
    data object A4Landscape : DocumentAspectRatio()
    data object LetterPortrait : DocumentAspectRatio()
    data object LetterLandscape : DocumentAspectRatio()
    data class Custom(val width: Float, val height: Float) : DocumentAspectRatio()
}

data class WarpOptions(
    val aspectRatio: DocumentAspectRatio = DocumentAspectRatio.Auto,
    val maxOutputSide: Int = 2400,
)

enum class DocumentFilter {
    ORIGINAL, AUTO, GRAY, BW;

    companion object {
        fun fromName(value: String): DocumentFilter = when (value.trim().uppercase()) {
            "ORIGINAL", "ORIG", "NONE" -> ORIGINAL
            "AUTO" -> AUTO
            "GRAY", "GREY", "GRAYSCALE" -> GRAY
            "BW", "B/W", "BLACK_WHITE", "BLACKWHITE", "ЧБ", "Ч-Б" -> BW
            else -> AUTO
        }
    }
}

class ImageProcessor {

    fun processDocument(
        bitmap: Bitmap,
        corners: List<PointF>,
        warpOptions: WarpOptions = WarpOptions(),
        filter: DocumentFilter = DocumentFilter.AUTO,
    ): Bitmap {
        val warped = warpPerspective(bitmap, corners, warpOptions)
        return applyFilter(warped, filter)
    }

    fun warpPerspective(
        bitmap: Bitmap,
        corners: List<PointF>,
        options: WarpOptions = WarpOptions(),
    ): Bitmap {
        require(corners.size == POINT_COUNT) { "Для perspective warp нужно ровно 4 точки" }
        require(options.maxOutputSide > 0) { "maxOutputSide должен быть больше 0" }

        val orderedCorners = orderCorners(corners, bitmap.width, bitmap.height)
        val geometry = DocumentGeometry.fromCorners(orderedCorners)
        val outputSize = computeOutputSize(geometry, options)

        val source = bitmap.toRgbaMat()
        val destination = Mat(
            outputSize.height,
            outputSize.width,
            source.type(),
            WHITE_RGBA,
        )
        val sourcePoints = MatOfPoint2f(
            orderedCorners[0].toCvPoint(),
            orderedCorners[1].toCvPoint(),
            orderedCorners[2].toCvPoint(),
            orderedCorners[3].toCvPoint(),
        )
        val destinationPoints = MatOfPoint2f(
            CvPoint(0.0, 0.0),
            CvPoint((outputSize.width - 1).toDouble(), 0.0),
            CvPoint((outputSize.width - 1).toDouble(), (outputSize.height - 1).toDouble()),
            CvPoint(0.0, (outputSize.height - 1).toDouble()),
        )
        val transform = Imgproc.getPerspectiveTransform(sourcePoints, destinationPoints)

        return try {
            Imgproc.warpPerspective(
                source,
                destination,
                transform,
                Size(outputSize.width.toDouble(), outputSize.height.toDouble()),
                Imgproc.INTER_CUBIC,
                Core.BORDER_CONSTANT,
                WHITE_RGBA,
            )
            destination.toBitmap()
        } finally {
            source.release()
            destination.release()
            sourcePoints.release()
            destinationPoints.release()
            transform.release()
        }
    }

    fun applyFilter(bitmap: Bitmap, filter: String): Bitmap = applyFilter(bitmap, DocumentFilter.fromName(filter))

    fun applyFilter(bitmap: Bitmap, filter: DocumentFilter): Bitmap = when (filter) {
        DocumentFilter.ORIGINAL -> bitmap
        DocumentFilter.AUTO -> applyAutoFilter(bitmap)
        DocumentFilter.GRAY -> applyGrayFilter(bitmap)
        DocumentFilter.BW -> applyBlackWhiteFilter(bitmap)
    }

    fun rotate90(bitmap: Bitmap): Bitmap = Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(90f) }, true
    )

    fun rotate90Ccw(bitmap: Bitmap): Bitmap = Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(-90f) }, true
    )

    private fun applyAutoFilter(bitmap: Bitmap): Bitmap {
        val source = bitmap.toRgbaMat()
        val rgb = Mat()
        val balanced = Mat()
        val clahe = Mat()
        val saturated = Mat()
        val blurred = Mat()
        val sharpened = Mat()
        val output = Mat()

        return try {
            Imgproc.cvtColor(source, rgb, Imgproc.COLOR_RGBA2RGB)
            grayWorldWhiteBalance(rgb, balanced)
            applyClaheToLuminance(balanced, clahe)
            boostSaturation(clahe, saturated)
            Imgproc.GaussianBlur(saturated, blurred, Size(0.0, 0.0), UNSHARP_SIGMA)
            Core.addWeighted(saturated, UNSHARP_ORIGINAL_WEIGHT, blurred, UNSHARP_BLUR_WEIGHT, 0.0, sharpened)
            Imgproc.cvtColor(sharpened, output, Imgproc.COLOR_RGB2RGBA)
            output.toBitmap()
        } finally {
            source.release()
            rgb.release()
            balanced.release()
            clahe.release()
            saturated.release()
            blurred.release()
            sharpened.release()
            output.release()
        }
    }

    private fun applyGrayFilter(bitmap: Bitmap): Bitmap {
        val source = bitmap.toRgbaMat()
        val gray = Mat()
        val enhanced = Mat()
        val output = Mat()
        val clahe = Imgproc.createCLAHE(CLAHE_CLIP_LIMIT, Size(CLAHE_GRID_SIZE, CLAHE_GRID_SIZE))

        return try {
            Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGBA2GRAY)
            clahe.apply(gray, enhanced)
            Imgproc.cvtColor(enhanced, output, Imgproc.COLOR_GRAY2RGBA)
            output.toBitmap()
        } finally {
            clahe.collectGarbage()
            source.release()
            gray.release()
            enhanced.release()
            output.release()
        }
    }

    private fun applyBlackWhiteFilter(bitmap: Bitmap): Bitmap {
        val source = bitmap.toRgbaMat()
        val gray = Mat()
        val background = Mat()
        val normalized = Mat()
        val enhanced = Mat()
        val binary = Mat()
        val cleaned = Mat()
        val output = Mat()
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(2.0, 2.0))
        val clahe = Imgproc.createCLAHE(CLAHE_CLIP_LIMIT, Size(CLAHE_GRID_SIZE, CLAHE_GRID_SIZE))

        return try {
            Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGBA2GRAY)
            Imgproc.GaussianBlur(gray, background, Size(0.0, 0.0), BACKGROUND_BLUR_SIGMA)
            Core.add(background, Scalar.all(1.0), background)
            Core.divide(gray, background, normalized, 255.0)
            normalized.convertTo(normalized, CvType.CV_8UC1)
            clahe.apply(normalized, enhanced)
            Imgproc.adaptiveThreshold(
                enhanced,
                binary,
                255.0,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                ADAPTIVE_BLOCK_SIZE,
                ADAPTIVE_C,
            )
            Imgproc.morphologyEx(binary, cleaned, Imgproc.MORPH_OPEN, kernel)
            Imgproc.cvtColor(cleaned, output, Imgproc.COLOR_GRAY2RGBA)
            output.toBitmap()
        } finally {
            clahe.collectGarbage()
            source.release()
            gray.release()
            background.release()
            normalized.release()
            enhanced.release()
            binary.release()
            cleaned.release()
            output.release()
            kernel.release()
        }
    }

    private fun grayWorldWhiteBalance(inputRgb: Mat, outputRgb: Mat) {
        val channels = ArrayList<Mat>(RGB_CHANNELS)
        val adjustedChannels = ArrayList<Mat>(RGB_CHANNELS)

        try {
            Core.split(inputRgb, channels)
            val means = channels.map { Core.mean(it).`val`[0] }
            val average = means.average().coerceAtLeast(1.0)

            channels.forEachIndexed { index, channel ->
                val adjusted = Mat()
                val alpha = average / means[index].coerceAtLeast(1.0)
                channel.convertTo(adjusted, -1, alpha, 0.0)
                adjustedChannels.add(adjusted)
            }

            Core.merge(adjustedChannels, outputRgb)
        } finally {
            channels.forEach { it.release() }
            adjustedChannels.forEach { it.release() }
        }
    }

    private fun applyClaheToLuminance(inputRgb: Mat, outputRgb: Mat) {
        val lab = Mat()
        val enhancedLab = Mat()
        val channels = ArrayList<Mat>(RGB_CHANNELS)
        val clahe = Imgproc.createCLAHE(CLAHE_CLIP_LIMIT, Size(CLAHE_GRID_SIZE, CLAHE_GRID_SIZE))

        try {
            Imgproc.cvtColor(inputRgb, lab, Imgproc.COLOR_RGB2Lab)
            Core.split(lab, channels)
            val enhancedL = Mat()
            clahe.apply(channels[0], enhancedL)
            channels[0].release()
            channels[0] = enhancedL
            Core.merge(channels, enhancedLab)
            Imgproc.cvtColor(enhancedLab, outputRgb, Imgproc.COLOR_Lab2RGB)
        } finally {
            clahe.collectGarbage()
            lab.release()
            enhancedLab.release()
            channels.forEach { it.release() }
        }
    }

    private fun boostSaturation(inputRgb: Mat, outputRgb: Mat) {
        val hsv = Mat()
        val channels = ArrayList<Mat>(RGB_CHANNELS)

        try {
            Imgproc.cvtColor(inputRgb, hsv, Imgproc.COLOR_RGB2HSV)
            Core.split(hsv, channels)
            val boostedSaturation = Mat()
            channels[1].convertTo(boostedSaturation, -1, SATURATION_BOOST, 0.0)
            channels[1].release()
            channels[1] = boostedSaturation
            Core.merge(channels, hsv)
            Imgproc.cvtColor(hsv, outputRgb, Imgproc.COLOR_HSV2RGB)
        } finally {
            hsv.release()
            channels.forEach { it.release() }
        }
    }

    private fun orderCorners(corners: List<PointF>, width: Int, height: Int): List<PointF> {
        val clamped = corners.map { point ->
            require(point.x.isFinite() && point.y.isFinite()) { "Координаты углов должны быть конечными" }
            PointF(
                point.x.coerceIn(0f, maxOf(width - 1, 0).toFloat()),
                point.y.coerceIn(0f, maxOf(height - 1, 0).toFloat()),
            )
        }

        val topLeft = clamped.minBy { it.x + it.y }
        val bottomRight = clamped.maxBy { it.x + it.y }
        val topRight = clamped.minBy { it.y - it.x }
        val bottomLeft = clamped.maxBy { it.y - it.x }
        val ordered = listOf(topLeft, topRight, bottomRight, bottomLeft)

        return if (ordered.distinctBy { "${it.x}:${it.y}" }.size == POINT_COUNT) {
            ordered
        } else {
            orderCornersByRows(clamped)
        }
    }

    private fun orderCornersByRows(corners: List<PointF>): List<PointF> {
        val sortedByY = corners.sortedBy { it.y }
        val top = sortedByY.take(2).sortedBy { it.x }
        val bottom = sortedByY.takeLast(2).sortedBy { it.x }
        return listOf(top[0], top[1], bottom[1], bottom[0])
    }

    private fun computeOutputSize(
        geometry: DocumentGeometry,
        options: WarpOptions,
    ): OutputSize {
        val rawRatio = aspectRatio(geometry, options.aspectRatio)
        val ratio = rawRatio.coerceAtLeast(MIN_ASPECT_RATIO)

        val (rawWidth, rawHeight) = if (options.aspectRatio is DocumentAspectRatio.Auto) {
            geometry.width to geometry.height
        } else if (ratio >= 1f) {
            val width = maxOf(geometry.width, geometry.height)
            width to width / ratio
        } else {
            val height = maxOf(geometry.width, geometry.height)
            height * ratio to height
        }

        val longSide = maxOf(rawWidth, rawHeight).coerceAtLeast(1f)
        val scale = minOf(1f, options.maxOutputSide / longSide)

        return OutputSize(
            width = maxOf(1, (rawWidth * scale).roundToInt()),
            height = maxOf(1, (rawHeight * scale).roundToInt()),
        )
    }

    private fun aspectRatio(
        geometry: DocumentGeometry,
        aspectRatio: DocumentAspectRatio,
    ): Float = when (aspectRatio) {
        DocumentAspectRatio.Auto -> geometry.width / geometry.height.coerceAtLeast(1f)
        DocumentAspectRatio.A4Portrait -> A4_SHORT_SIDE / A4_LONG_SIDE
        DocumentAspectRatio.A4Landscape -> A4_LONG_SIDE / A4_SHORT_SIDE
        DocumentAspectRatio.LetterPortrait -> LETTER_SHORT_SIDE / LETTER_LONG_SIDE
        DocumentAspectRatio.LetterLandscape -> LETTER_LONG_SIDE / LETTER_SHORT_SIDE
        is DocumentAspectRatio.Custom -> {
            require(aspectRatio.width > 0f && aspectRatio.height > 0f) {
                "Кастомное соотношение сторон должно иметь положительные ширину и высоту"
            }
            aspectRatio.width / aspectRatio.height
        }
    }

    private fun Bitmap.toRgbaMat(): Mat {
        val sourceBitmap = if (config == Bitmap.Config.ARGB_8888) {
            this
        } else {
            copy(Bitmap.Config.ARGB_8888, false)
        }
        val mat = Mat()
        Utils.bitmapToMat(sourceBitmap, mat)
        if (sourceBitmap !== this) sourceBitmap.recycle()
        return mat
    }

    private fun Mat.toBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(cols(), rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(this, bitmap)
        return bitmap
    }

    private fun PointF.toCvPoint(): CvPoint = CvPoint(x.toDouble(), y.toDouble())

    private data class DocumentGeometry(
        val width: Float,
        val height: Float,
    ) {
        companion object {
            fun fromCorners(corners: List<PointF>): DocumentGeometry {
                val topWidth = distance(corners[0], corners[1])
                val bottomWidth = distance(corners[3], corners[2])
                val leftHeight = distance(corners[0], corners[3])
                val rightHeight = distance(corners[1], corners[2])

                return DocumentGeometry(
                    width = maxOf(topWidth, bottomWidth, 1f),
                    height = maxOf(leftHeight, rightHeight, 1f),
                )
            }

            private fun distance(a: PointF, b: PointF): Float =
                hypot((a.x - b.x).toDouble(), (a.y - b.y).toDouble()).toFloat()
        }
    }

    private data class OutputSize(
        val width: Int,
        val height: Int,
    )

    private companion object {
        const val POINT_COUNT = 4
        const val RGB_CHANNELS = 3
        const val CLAHE_CLIP_LIMIT = 2.0
        const val CLAHE_GRID_SIZE = 8.0
        const val SATURATION_BOOST = 1.08
        const val UNSHARP_SIGMA = 1.0
        const val UNSHARP_ORIGINAL_WEIGHT = 1.35
        const val UNSHARP_BLUR_WEIGHT = -0.35
        const val BACKGROUND_BLUR_SIGMA = 25.0
        const val ADAPTIVE_BLOCK_SIZE = 31
        const val ADAPTIVE_C = 12.0
        const val MIN_ASPECT_RATIO = 0.05f
        const val A4_SHORT_SIDE = 1f
        val A4_LONG_SIDE = sqrt(2.0).toFloat()
        const val LETTER_SHORT_SIDE = 8.5f
        const val LETTER_LONG_SIDE = 11f
        val WHITE_RGBA = Scalar(255.0, 255.0, 255.0, 255.0)
    }
}

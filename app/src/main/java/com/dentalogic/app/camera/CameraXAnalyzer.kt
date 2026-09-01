package com.dentalogic.app.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.dentalogic.app.core.DetectionResult
import com.dentalogic.app.inference.YoloOnnxDetector

/**
 * CameraX ImageAnalysis analyzer running ONNX detection on each incoming video frame.
 */
class CameraXAnalyzer(
    private val detector: YoloOnnxDetector,
    private val onDetectionsUpdated: (List<DetectionResult>, Int, Int, Long) -> Unit,
) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val startTime = System.currentTimeMillis()

        try {
            val bitmap = imageProxy.toBitmap()
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            val rotatedBitmap = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            val results = detector.detect(rotatedBitmap)
            val duration = System.currentTimeMillis() - startTime

            onDetectionsUpdated(results, rotatedBitmap.width, rotatedBitmap.height, duration)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }
}

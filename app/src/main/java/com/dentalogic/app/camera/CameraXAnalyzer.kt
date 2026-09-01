package com.dentalogic.app.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.dentalogic.app.core.DetectionResult
import com.dentalogic.app.inference.YoloOnnxDetector

/**
 * CameraX ImageAnalysis analyzer running ONNX detection on incoming video frames.
 * Equipped with isStopped guard for crash-free screen teardown.
 */
class CameraXAnalyzer(
    private val detector: YoloOnnxDetector,
    private val onDetectionsUpdated: (List<DetectionResult>, Int, Int, Long) -> Unit,
) : ImageAnalysis.Analyzer {

    @Volatile
    var isStopped: Boolean = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isStopped) {
            imageProxy.close()
            return
        }

        val startTime = System.currentTimeMillis()
        try {
            val bitmap = imageProxy.toBitmap()
            if (isStopped) return

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val rotatedBitmap = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            if (isStopped) return

            val results = detector.detect(rotatedBitmap)
            val duration = System.currentTimeMillis() - startTime

            if (!isStopped) {
                onDetectionsUpdated(results, rotatedBitmap.width, rotatedBitmap.height, duration)
            }
        } catch (e: Exception) {
            if (!isStopped) {
                Log.e("CameraXAnalyzer", "Frame analysis error", e)
            }
        } finally {
            try {
                imageProxy.close()
            } catch (_: Exception) {}
        }
    }
}

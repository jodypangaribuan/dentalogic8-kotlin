package com.dentalogic.app.inference

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import com.dentalogic.app.core.DetectionResult
import java.nio.FloatBuffer

/**
 * YOLO ONNX runtime detector for dental caries classification and localization.
 */
class YoloOnnxDetector(context: Context) {

    private val ortEnv: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var ortSession: OrtSession? = null

    companion object {
        const val MODEL_INPUT_SIZE = 640
        const val MODEL_FILE_NAME = "best_opset21.onnx"
        val CARIES_CLASSES = listOf("D0", "D1", "D2", "D3", "D4", "D5", "D6")
        const val CONFIDENCE_THRESHOLD = 0.25f
        const val IOU_THRESHOLD = 0.50f
    }

    init {
        try {
            val modelBytes = context.assets.open(MODEL_FILE_NAME).readBytes()
            val sessionOptions = OrtSession.SessionOptions().apply {
                setIntraOpNumThreads(4)
            }
            ortSession = ortEnv.createSession(modelBytes, sessionOptions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Runs inference on the input bitmap and returns bounding boxes of detected caries.
     */
    fun detect(bitmap: Bitmap): List<DetectionResult> {
        val session = ortSession ?: return emptyList()
        val resizedBitmap = if (bitmap.width != MODEL_INPUT_SIZE || bitmap.height != MODEL_INPUT_SIZE) {
            Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, true)
        } else {
            bitmap
        }

        val floatBuffer = bitmapToFloatBuffer(resizedBitmap)
        val inputShape = longArrayOf(1, 3, MODEL_INPUT_SIZE.toLong(), MODEL_INPUT_SIZE.toLong())

        val inputTensor = OnnxTensor.createTensor(ortEnv, floatBuffer, inputShape)

        try {
            val inputName = session.inputNames.iterator().next()
            val outputResults = session.run(mapOf(inputName to inputTensor))

            @Suppress("UNCHECKED_CAST")
            val outputTensor = outputResults.get(0).value as Array<Array<FloatArray>>
            return processYoloOutput(outputTensor, bitmap.width, bitmap.height)
        } finally {
            inputTensor.close()
        }
    }

    private fun bitmapToFloatBuffer(bitmap: Bitmap): FloatBuffer {
        val buffer = FloatBuffer.allocate(1 * 3 * MODEL_INPUT_SIZE * MODEL_INPUT_SIZE)
        val intValues = IntArray(MODEL_INPUT_SIZE * MODEL_INPUT_SIZE)
        bitmap.getPixels(intValues, 0, MODEL_INPUT_SIZE, 0, 0, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE)

        // CHW format ordering
        val rOffset = 0
        val gOffset = MODEL_INPUT_SIZE * MODEL_INPUT_SIZE
        val bOffset = 2 * MODEL_INPUT_SIZE * MODEL_INPUT_SIZE

        for (i in intValues.indices) {
            val pixel = intValues[i]
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            buffer.put(rOffset + i, r)
            buffer.put(gOffset + i, g)
            buffer.put(bOffset + i, b)
        }

        buffer.rewind()
        return buffer
    }

    private fun processYoloOutput(
        output: Array<Array<FloatArray>>,
        origWidth: Int,
        origHeight: Int,
    ): List<DetectionResult> {
        val batch = output[0]
        val numDetections = batch[0].size
        val numClasses = CARIES_CLASSES.size

        val scaleX = origWidth.toFloat() / MODEL_INPUT_SIZE
        val scaleY = origHeight.toFloat() / MODEL_INPUT_SIZE

        val boxes = mutableListOf<RectF>()
        val scores = mutableListOf<Float>()
        val classIds = mutableListOf<Int>()

        for (i in 0 until numDetections) {
            val cx = batch[0][i]
            val cy = batch[1][i]
            val w = batch[2][i]
            val h = batch[3][i]

            var maxScore = 0f
            var maxClassId = -1

            for (c in 0 until numClasses) {
                val score = batch[4 + c][i]
                if (score > maxScore) {
                    maxScore = score
                    maxClassId = c
                }
            }

            if (maxScore >= CONFIDENCE_THRESHOLD && maxClassId >= 0) {
                val x1 = (cx - w / 2f) * scaleX
                val y1 = (cy - h / 2f) * scaleY
                val x2 = (cx + w / 2f) * scaleX
                val y2 = (cy + h / 2f) * scaleY

                boxes.add(RectF(x1, y1, x2, y2))
                scores.add(maxScore)
                classIds.add(maxClassId)
            }
        }

        return nms(boxes, scores, classIds, IOU_THRESHOLD)
    }

    private fun nms(
        boxes: List<RectF>,
        scores: List<Float>,
        classIds: List<Int>,
        iouThreshold: Float,
    ): List<DetectionResult> {
        if (boxes.isEmpty()) return emptyList()

        val indices = scores.indices.sortedByDescending { scores[it] }.toMutableList()
        val results = mutableListOf<DetectionResult>()

        while (indices.isNotEmpty()) {
            val current = indices.removeAt(0)
            val currentBox = boxes[current]

            results.add(
                DetectionResult(
                    boundingBox = currentBox,
                    className = CARIES_CLASSES[classIds[current]],
                    classId = classIds[current],
                    confidence = scores[current],
                ),
            )

            val iterator = indices.iterator()
            while (iterator.hasNext()) {
                val nextIdx = iterator.next()
                val iou = calculateIoU(currentBox, boxes[nextIdx])
                if (iou > iouThreshold) {
                    iterator.remove()
                }
            }
        }

        return results
    }

    private fun calculateIoU(a: RectF, b: RectF): Float {
        val interLeft = maxOf(a.left, b.left)
        val interTop = maxOf(a.top, b.top)
        val interRight = minOf(a.right, b.right)
        val interBottom = minOf(a.bottom, b.bottom)

        if (interLeft >= interRight || interTop >= interBottom) return 0f

        val intersection = (interRight - interLeft) * (interBottom - interTop)
        val areaA = a.width() * a.height()
        val areaB = b.width() * b.height()

        return intersection / (areaA + areaB - intersection)
    }

    fun close() {
        ortSession?.close()
        ortEnv.close()
    }
}

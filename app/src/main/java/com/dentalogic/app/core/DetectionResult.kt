package com.dentalogic.app.core

import android.graphics.RectF

/**
 * Data class representing a detected caries bounding box from inference.
 *
 * @param boundingBox RectF in pixel coordinates relative to original image/view size (x1, y1, x2, y2)
 * @param className Class name label ('D0', 'D1', 'D2', 'D3', 'D4', 'D5', 'D6')
 * @param classId Index of the class (0..6)
 * @param confidence Confidence score between 0.0 and 1.0
 */
data class DetectionResult(
    val boundingBox: RectF,
    val className: String,
    val classId: Int,
    val confidence: Float,
)

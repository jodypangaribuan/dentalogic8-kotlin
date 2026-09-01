package com.dentalogic.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.dentalogic.app.core.DentalCondition
import com.dentalogic.app.core.DetectionResult

/**
 * Real-time bounding box canvas overlay for rendering detected caries labels and bounding boxes over Camera preview.
 */
@Composable
fun BoundingBoxOverlay(
    detections: List<DetectionResult>,
    imageWidth: Int,
    imageHeight: Int,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxSize()) {
        if (imageWidth <= 0 || imageHeight <= 0) return@Canvas

        val scale = maxOf(size.width / imageWidth.toFloat(), size.height / imageHeight.toFloat())
        val offsetX = (size.width - imageWidth * scale) / 2f
        val offsetY = (size.height - imageHeight * scale) / 2f

        for (detection in detections) {
            val box = detection.boundingBox
            val left = box.left * scale + offsetX
            val top = box.top * scale + offsetY
            val right = box.right * scale + offsetX
            val bottom = box.bottom * scale + offsetY

            val condition = DentalCondition.fromClassName(detection.className)
            val color = condition.color

            // Rounded bounding box outline
            drawRoundRect(
                color = color,
                topLeft = Offset(left, top),
                size = Size(right - left, bottom - top),
                cornerRadius = CornerRadius(8f, 8f),
                style = Stroke(width = 3f),
            )

            // Label badge
            val label = "${detection.className} ${(detection.confidence * 100).toInt()}%"
            val textLayoutResult = textMeasurer.measure(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            val badgeWidth = textLayoutResult.size.width + 16f
            val badgeHeight = textLayoutResult.size.height + 8f
            val badgeTop = if (top - badgeHeight - 4f < 0) top + 4f else top - badgeHeight - 4f

            drawRoundRect(
                color = color.copy(alpha = 0.9f),
                topLeft = Offset(left, badgeTop),
                size = Size(badgeWidth, badgeHeight),
                cornerRadius = CornerRadius(6f, 6f),
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(left + 8f, badgeTop + 4f),
            )
        }
    }
}

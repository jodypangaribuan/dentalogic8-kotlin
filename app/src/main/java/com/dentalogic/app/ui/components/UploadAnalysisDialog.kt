package com.dentalogic.app.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dentalogic.app.core.DentalCondition
import com.dentalogic.app.core.DetectionResult

/**
 * Dialog displaying static image AI analysis with bounding box overlay and ICDAS classification results.
 */
@Composable
fun UploadAnalysisDialog(
    bitmap: Bitmap,
    detections: List<DetectionResult>,
    onDismiss: () -> Unit,
    onPickAnother: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "Analysis Result",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = if (detections.isEmpty()) "No dental caries detected" else "${detections.size} Caries Detected",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Image with Bounding Box Overlay
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(
                            if (bitmap.height > 0) bitmap.width.toFloat() / bitmap.height.toFloat() else 1f,
                        ),
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Uploaded Dental Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        BoundingBoxOverlay(
                            detections = detections,
                            imageWidth = bitmap.width,
                            imageHeight = bitmap.height,
                            modifier = Modifier.matchParentSize(),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Detection Findings Chips
                if (detections.isNotEmpty()) {
                    val grouped = detections.groupBy { it.className }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        items(grouped.keys.toList().sorted()) { code ->
                            val count = grouped[code]?.size ?: 0
                            val cond = DentalCondition.fromClassName(code)
                            AssistChip(
                                onClick = {},
                                label = { Text("$code: ${cond.title} ($count)", fontSize = 12.sp) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = cond.color.copy(alpha = 0.12f),
                                    labelColor = cond.color,
                                ),
                                border = null,
                                shape = RoundedCornerShape(12.dp),
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Healthy tooth surface (D0). No enamel or dentin lesions identified.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(
                        onClick = onPickAnother,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("New Photo", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Done", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

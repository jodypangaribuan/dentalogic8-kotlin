package com.dentalogic.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dentalogic.app.core.DentalCondition
import com.dentalogic.app.data.ScanRecord
import com.dentalogic.app.ui.components.BoundingBoxOverlay
import java.io.File

/**
 * Full page displaying complete analysis results, the scanned image result,
 * clinical recommendations, and ICDAS lesion breakdown.
 */
@Composable
fun ScanDetailScreen(
    record: ScanRecord,
    contentPadding: PaddingValues,
    onDelete: () -> Unit,
) {
    val condition = DentalCondition.fromClassName(record.highestSeverity)

    val imageBitmap = remember(record.imagePath) {
        record.imagePath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                try {
                    BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                } catch (_: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Page Title & Timestamp
        item(key = "header") {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "Scan Details",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = record.dateFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Image Result Card with BoundingBoxOverlay
        item(key = "scanned_image") {
            if (imageBitmap != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                if (imageBitmap.height > 0) imageBitmap.width.toFloat() / imageBitmap.height.toFloat() else 1f,
                            ),
                    ) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Scanned tooth specimen",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (record.detections.isNotEmpty()) {
                            BoundingBoxOverlay(
                                detections = record.detections,
                                imageWidth = imageBitmap.width,
                                imageHeight = imageBitmap.height,
                                modifier = Modifier.matchParentSize(),
                            )
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ImageNotSupported,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Column {
                            Text(
                                text = "Camera Frame Snapshot",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "Analyzed on-device via YOLOv12 engine",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        // Clinical Risk Assessment Banner
        item(key = "risk_banner") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = condition.color.copy(alpha = 0.12f),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(condition.color),
                        )
                        Text(
                            text = "${record.riskLevel} Caries Risk Level",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = condition.color,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = getClinicalRecommendation(record.highestSeverity),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp,
                    )
                }
            }
        }

        // Primary Finding Card
        item(key = "primary_finding") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "PRIMARY FINDING",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.8.sp,
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = condition.color.copy(alpha = 0.2f),
                        ) {
                            Text(
                                text = condition.severityLevel,
                                color = condition.color,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${condition.code} · ${condition.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = condition.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                    )
                }
            }
        }

        // Breakdown of detected lesions
        item(key = "breakdown") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "DETECTED LESIONS (${record.totalDetections} TOTAL)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.8.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (record.cariesCounts.isEmpty()) {
                        Text(
                            text = "No caries findings recorded for this scan.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        val sortedKeys = record.cariesCounts.keys.toList().sorted()
                        sortedKeys.forEachIndexed { index, code ->
                            val count = record.cariesCounts[code] ?: 0
                            val itemCond = DentalCondition.fromClassName(code)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(itemCond.color),
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${itemCond.code} · ${itemCond.title}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            text = itemCond.severityLevel,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = itemCond.color.copy(alpha = 0.15f),
                                ) {
                                    Text(
                                        text = "$count ${if (count > 1) "lesions" else "lesion"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = itemCond.color,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    )
                                }
                            }
                            if (index < sortedKeys.size - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                )
                            }
                        }
                    }
                }
            }
        }

        // Delete Button
        item(key = "delete_action") {
            OutlinedButton(
                onClick = onDelete,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete This Record",
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

private fun getClinicalRecommendation(highestSeverity: String): String = when (highestSeverity.trim().uppercase()) {
    "D0", "HEALTHY" ->
        "Enamel is intact and sound. Maintain daily oral hygiene with fluoridated toothpaste and routine dental visits."
    "D1", "D2" ->
        "Initial stage enamel lesion. Non-invasive remineralization therapy, topical fluoride varnish, and biofilm control are recommended to arrest progression."
    "D3", "D4" ->
        "Localized micro-cavitation or dentinal involvement. Professional dental assessment and conservative intervention recommended."
    "D5", "D6" ->
        "Extensive cavitated caries with significant dentin or pulp exposure. Prompt restorative treatment and professional consultation strongly advised."
    else ->
        "Maintain routine dental care and consult your dental healthcare professional."
}

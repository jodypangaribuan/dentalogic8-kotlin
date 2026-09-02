package com.dentalogic.app.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import com.dentalogic.app.core.DetectionResult
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Scan history item record with associated detections and snapshot path.
 */
data class ScanRecord(
    val id: String,
    val timestamp: Long,
    val dateFormatted: String,
    val totalDetections: Int,
    val highestSeverity: String, // 'D0', 'D1', ..., 'D6' or 'Healthy'
    val cariesCounts: Map<String, Int>, // "D0" -> 3, "D1" -> 1, etc.
    val riskLevel: String, // "Low", "Medium", "High"
    val imagePath: String? = null,
    val detections: List<DetectionResult> = emptyList(),
)

/**
 * Repository for managing persistent scan history records, image snapshots, and bounding boxes in local storage.
 */
class ScanHistoryRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("dentalogic_history", Context.MODE_PRIVATE)

    /**
     * Retrieves all saved scan records in reverse chronological order.
     * Returns empty list if no user scans have occurred yet.
     */
    fun getRecords(): List<ScanRecord> {
        val jsonStr = prefs.getString("records_json", null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<ScanRecord>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val cariesObj = obj.getJSONObject("cariesCounts")
                val cariesMap = mutableMapOf<String, Int>()
                cariesObj.keys().forEach { key ->
                    cariesMap[key] = cariesObj.getInt(key)
                }

                val imgPath = obj.optString("imagePath").takeIf { it.isNotBlank() }

                val detArray = obj.optJSONArray("detections")
                val detList = mutableListOf<DetectionResult>()
                if (detArray != null) {
                    for (j in 0 until detArray.length()) {
                        val detObj = detArray.getJSONObject(j)
                        val rect = RectF(
                            detObj.getDouble("left").toFloat(),
                            detObj.getDouble("top").toFloat(),
                            detObj.getDouble("right").toFloat(),
                            detObj.getDouble("bottom").toFloat(),
                        )
                        detList.add(
                            DetectionResult(
                                boundingBox = rect,
                                className = detObj.getString("className"),
                                classId = detObj.getInt("classId"),
                                confidence = detObj.getDouble("confidence").toFloat(),
                            ),
                        )
                    }
                }

                list.add(
                    ScanRecord(
                        id = obj.getString("id"),
                        timestamp = obj.getLong("timestamp"),
                        dateFormatted = obj.getString("dateFormatted"),
                        totalDetections = obj.getInt("totalDetections"),
                        highestSeverity = obj.getString("highestSeverity"),
                        cariesCounts = cariesMap,
                        riskLevel = obj.getString("riskLevel"),
                        imagePath = imgPath,
                        detections = detList,
                    ),
                )
            }
            list.sortedByDescending { it.timestamp }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Saves a new scan detection record along with bounding boxes and an optional captured image snapshot.
     */
    fun saveRecord(detections: List<DetectionResult>, bitmap: Bitmap? = null) {
        val records = getRecords().toMutableList()
        val timestamp = System.currentTimeMillis()
        val dateFormatted = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))

        var savedImagePath: String? = null
        if (bitmap != null) {
            try {
                val scansDir = File(context.filesDir, "scans").apply { mkdirs() }
                val imgFile = File(scansDir, "scan_${timestamp}.jpg")
                FileOutputStream(imgFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                savedImagePath = imgFile.absolutePath
            } catch (e: Exception) {
                Log.e("ScanHistoryRepository", "Failed to persist scan snapshot image", e)
            }
        }

        val cariesMap = mutableMapOf<String, Int>()
        var highestSeverity = "Healthy"
        var maxSevIndex = -1

        val cariesClasses = listOf("D0", "D1", "D2", "D3", "D4", "D5", "D6")
        for (det in detections) {
            cariesMap[det.className] = (cariesMap[det.className] ?: 0) + 1
            val idx = cariesClasses.indexOf(det.className)
            if (idx > maxSevIndex) {
                maxSevIndex = idx
                highestSeverity = det.className
            }
        }

        val riskLevel = when {
            maxSevIndex >= 4 -> "High" // D4-D6
            maxSevIndex >= 2 -> "Medium" // D2-D3
            maxSevIndex >= 0 -> "Low" // D0-D1
            else -> "Healthy"
        }

        val newRecord = ScanRecord(
            id = "SCAN_${timestamp}",
            timestamp = timestamp,
            dateFormatted = dateFormatted,
            totalDetections = detections.size,
            highestSeverity = highestSeverity,
            cariesCounts = cariesMap,
            riskLevel = riskLevel,
            imagePath = savedImagePath,
            detections = detections,
        )

        records.add(0, newRecord)

        val jsonArray = JSONArray()
        for (rec in records.take(50)) { // Keep last 50
            val obj = JSONObject()
            obj.put("id", rec.id)
            obj.put("timestamp", rec.timestamp)
            obj.put("dateFormatted", rec.dateFormatted)
            obj.put("totalDetections", rec.totalDetections)
            obj.put("highestSeverity", rec.highestSeverity)
            obj.put("riskLevel", rec.riskLevel)
            obj.put("imagePath", rec.imagePath ?: "")

            val cariesObj = JSONObject()
            rec.cariesCounts.forEach { (k, v) -> cariesObj.put(k, v) }
            obj.put("cariesCounts", cariesObj)

            val detArray = JSONArray()
            for (det in rec.detections) {
                val detObj = JSONObject()
                detObj.put("left", det.boundingBox.left.toDouble())
                detObj.put("top", det.boundingBox.top.toDouble())
                detObj.put("right", det.boundingBox.right.toDouble())
                detObj.put("bottom", det.boundingBox.bottom.toDouble())
                detObj.put("className", det.className)
                detObj.put("classId", det.classId)
                detObj.put("confidence", det.confidence.toDouble())
                detArray.put(detObj)
            }
            obj.put("detections", detArray)

            jsonArray.put(obj)
        }

        prefs.edit().putString("records_json", jsonArray.toString()).apply()
    }

    /**
     * Deletes a specific scan record and its associated snapshot image.
     */
    fun deleteRecord(id: String) {
        val records = getRecords().toMutableList()
        val toDelete = records.find { it.id == id }
        if (toDelete?.imagePath != null) {
            try {
                File(toDelete.imagePath).delete()
            } catch (e: Exception) {
                Log.e("ScanHistoryRepository", "Failed to delete image file", e)
            }
        }

        records.removeAll { it.id == id }

        val jsonArray = JSONArray()
        for (rec in records) {
            val obj = JSONObject()
            obj.put("id", rec.id)
            obj.put("timestamp", rec.timestamp)
            obj.put("dateFormatted", rec.dateFormatted)
            obj.put("totalDetections", rec.totalDetections)
            obj.put("highestSeverity", rec.highestSeverity)
            obj.put("riskLevel", rec.riskLevel)
            obj.put("imagePath", rec.imagePath ?: "")

            val cariesObj = JSONObject()
            rec.cariesCounts.forEach { (k, v) -> cariesObj.put(k, v) }
            obj.put("cariesCounts", cariesObj)

            val detArray = JSONArray()
            for (det in rec.detections) {
                val detObj = JSONObject()
                detObj.put("left", det.boundingBox.left.toDouble())
                detObj.put("top", det.boundingBox.top.toDouble())
                detObj.put("right", det.boundingBox.right.toDouble())
                detObj.put("bottom", det.boundingBox.bottom.toDouble())
                detObj.put("className", det.className)
                detObj.put("classId", det.classId)
                detObj.put("confidence", det.confidence.toDouble())
                detArray.put(detObj)
            }
            obj.put("detections", detArray)

            jsonArray.put(obj)
        }

        prefs.edit().putString("records_json", jsonArray.toString()).apply()
    }

    /**
     * Clears all stored scan records and files.
     */
    fun clearRecords() {
        val records = getRecords()
        for (rec in records) {
            if (rec.imagePath != null) {
                try {
                    File(rec.imagePath).delete()
                } catch (_: Exception) {}
            }
        }
        prefs.edit().remove("records_json").apply()
    }
}

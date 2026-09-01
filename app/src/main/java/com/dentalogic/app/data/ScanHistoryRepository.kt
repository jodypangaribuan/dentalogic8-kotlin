package com.dentalogic.app.data

import android.content.Context
import android.content.SharedPreferences
import com.dentalogic.app.core.DetectionResult
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Scan history item record.
 */
data class ScanRecord(
    val id: String,
    val timestamp: Long,
    val dateFormatted: String,
    val totalDetections: Int,
    val highestSeverity: String, // 'D0', 'D1', ..., 'D6' or 'Healthy'
    val cariesCounts: Map<String, Int>, // "D0" -> 3, "D1" -> 1, etc.
    val riskLevel: String, // "Low", "Medium", "High"
)

/**
 * Repository for managing persistent scan history records in local storage.
 */
class ScanHistoryRepository(context: Context) {

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

                list.add(
                    ScanRecord(
                        id = obj.getString("id"),
                        timestamp = obj.getLong("timestamp"),
                        dateFormatted = obj.getString("dateFormatted"),
                        totalDetections = obj.getInt("totalDetections"),
                        highestSeverity = obj.getString("highestSeverity"),
                        cariesCounts = cariesMap,
                        riskLevel = obj.getString("riskLevel"),
                    ),
                )
            }
            list.sortedByDescending { it.timestamp }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Saves a new scan detection record.
     */
    fun saveRecord(detections: List<DetectionResult>) {
        val records = getRecords().toMutableList()
        val timestamp = System.currentTimeMillis()
        val dateFormatted = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))

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

            val cariesObj = JSONObject()
            rec.cariesCounts.forEach { (k, v) -> cariesObj.put(k, v) }
            obj.put("cariesCounts", cariesObj)

            jsonArray.put(obj)
        }

        prefs.edit().putString("records_json", jsonArray.toString()).apply()
    }

    /**
     * Clears all stored scan records.
     */
    fun clearRecords() {
        prefs.edit().remove("records_json").apply()
    }
}

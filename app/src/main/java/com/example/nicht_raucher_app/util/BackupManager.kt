package com.example.nicht_raucher_app.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.nicht_raucher_app.domain.Habit
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object BackupManager {

    private const val FILE_NAME = "pureprogress_backup.json"

    fun exportToJson(context: Context, habits: List<Habit>): Boolean {
        return try {
            val json = habitsToJson(habits).toString(2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, FILE_NAME)
                    put(MediaStore.Downloads.MIME_TYPE, "application/json")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: return false
                resolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            } else {
                val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    ?: return false
                File(dir, FILE_NAME).writeText(json)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun importFromJson(context: Context, uri: Uri): List<Habit>? {
        return try {
            val json = context.contentResolver.openInputStream(uri)?.use {
                it.bufferedReader().readText()
            } ?: return null
            jsonToHabits(json)
        } catch (_: Exception) {
            null
        }
    }

    private fun habitsToJson(habits: List<Habit>): JSONArray {
        val array = JSONArray()
        habits.forEach { habit ->
            array.put(JSONObject().apply {
                put("label", habit.label)
                put("startTimeMillis", habit.startTimeMillis)
                put("unitsPerDay", habit.unitsPerDay)
                put("costPerUnit", habit.costPerUnit)
                put("unitName", habit.unitName)
                put("cardColor", habit.cardColor)
                put("position", habit.position)
                put("substanceType", habit.substanceType)
            })
        }
        return array
    }

    private fun jsonToHabits(json: String): List<Habit> {
        val array = JSONArray(json)
        return (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            Habit(
                id = 0,
                label = obj.getString("label"),
                startTimeMillis = obj.getLong("startTimeMillis"),
                unitsPerDay = obj.getDouble("unitsPerDay"),
                costPerUnit = obj.getDouble("costPerUnit"),
                unitName = obj.getString("unitName"),
                cardColor = obj.getInt("cardColor"),
                position = obj.getInt("position"),
                substanceType = obj.optString("substanceType", "CUSTOM")
            )
        }
    }
}
package com.example.nicht_raucher_app.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val startTimeMillis: Long,
    val unitsPerDay: Double,    // z.B. 20.0 Zigaretten, 5.0 Drinks, 3.0 Joints
    val costPerUnit: Double,    // Kosten pro Einheit in €
    val unitName: String,       // z.B. "Zigaretten", "Bier", "Joints", "Selbstgedrehte"
    val cardColor: Int,         // ARGB-Farbwert der Karte
    val position: Int = 0      // Reihenfolge im Dashboard
)
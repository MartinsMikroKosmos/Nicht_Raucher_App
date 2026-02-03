package com.example.nicht_raucher_app.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val startTimeMillis: Long,
    val cigarettesPerDay: Int,
    val packPrice: Double
)
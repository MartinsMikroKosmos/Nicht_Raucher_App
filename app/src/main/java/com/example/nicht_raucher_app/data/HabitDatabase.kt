package com.example.nicht_raucher_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nicht_raucher_app.domain.Habit

@Database(entities = [Habit::class], version = 3, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
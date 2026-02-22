package com.example.nicht_raucher_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nicht_raucher_app.domain.Habit

@Database(entities = [Habit::class], version = 4, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE habits ADD COLUMN substanceType TEXT NOT NULL DEFAULT 'CUSTOM'"
                )
            }
        }
    }
}
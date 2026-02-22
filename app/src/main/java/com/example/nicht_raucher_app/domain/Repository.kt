package com.example.nicht_raucher_app.domain

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun deleteHabit(habit: Habit)
    suspend fun updateHabitOrder(orderedHabits: List<Habit>)
    suspend fun updateHabit(habit: Habit)
}
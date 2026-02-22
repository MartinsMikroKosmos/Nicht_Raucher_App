package com.example.nicht_raucher_app.data

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val dao: HabitDao
) : Repository {
    override fun getAllHabits(): Flow<List<Habit>> = dao.getAllHabits()

    override suspend fun insertHabit(habit: Habit) = dao.insertHabit(habit)

    override suspend fun deleteHabit(habit: Habit) = dao.deleteHabit(habit)

    override suspend fun updateHabitOrder(orderedHabits: List<Habit>) {
        orderedHabits.forEachIndexed { index, habit ->
            dao.updateHabit(habit.copy(position = index))
        }
    }
}
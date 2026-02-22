package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(habit: Habit) = repository.updateHabit(habit)
}
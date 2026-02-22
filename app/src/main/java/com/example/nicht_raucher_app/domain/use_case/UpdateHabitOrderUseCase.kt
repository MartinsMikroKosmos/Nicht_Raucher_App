package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import javax.inject.Inject

class UpdateHabitOrderUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(orderedHabits: List<Habit>) =
        repository.updateHabitOrder(orderedHabits)
}
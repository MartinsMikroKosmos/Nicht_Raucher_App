package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(label: String, cigsPerDay: Int, packPrice: Double) {
        val newHabit = Habit(
            label = label,
            startTimeMillis = System.currentTimeMillis(),
            cigarettesPerDay = cigsPerDay,
            packPrice = packPrice
        )
        repository.insertHabit(newHabit)
    }
}
package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        label: String,
        unitsPerDay: Double,
        costPerUnit: Double,
        unitName: String,
        cardColor: Int,
        startTimeMillis: Long = System.currentTimeMillis()
    ) {
        val newHabit = Habit(
            label = label,
            startTimeMillis = startTimeMillis,
            unitsPerDay = unitsPerDay,
            costPerUnit = costPerUnit,
            unitName = unitName,
            cardColor = cardColor
        )
        repository.insertHabit(newHabit)
    }
}
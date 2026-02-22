package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import com.example.nicht_raucher_app.milestones.MilestoneScheduler
import javax.inject.Inject

class ImportHabitsUseCase @Inject constructor(
    private val repository: Repository,
    private val milestoneScheduler: MilestoneScheduler
) {
    suspend operator fun invoke(habits: List<Habit>) {
        habits.forEach { habit ->
            val newId = repository.insertHabit(habit.copy(id = 0))
            milestoneScheduler.scheduleMilestonesForHabit(
                habitId = newId.toInt(),
                habitLabel = habit.label,
                startTimeMillis = habit.startTimeMillis
            )
        }
    }
}
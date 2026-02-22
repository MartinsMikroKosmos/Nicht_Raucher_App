package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import com.example.nicht_raucher_app.milestones.MilestoneScheduler
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: Repository,
    private val milestoneScheduler: MilestoneScheduler
) {
    suspend operator fun invoke(habit: Habit) {
        milestoneScheduler.cancelMilestonesForHabit(habit.id)
        repository.deleteHabit(habit)
    }
}
package com.example.nicht_raucher_app.milestones

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.nicht_raucher_app.worker.MilestoneWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleMilestonesForHabit(habitId: Int, habitLabel: String, startTimeMillis: Long) {
        val workManager = WorkManager.getInstance(context)
        val now = System.currentTimeMillis()

        MilestoneData.milestones.forEach { milestone ->
            val triggerAt = startTimeMillis + milestone.durationMillis
            val delay = triggerAt - now

            if (delay > 0) {
                val inputData = workDataOf(
                    MilestoneWorker.KEY_HABIT_ID to habitId,
                    MilestoneWorker.KEY_HABIT_LABEL to habitLabel,
                    MilestoneWorker.KEY_MILESTONE_TITLE to milestone.title,
                    MilestoneWorker.KEY_MILESTONE_BODY to milestone.body
                )

                val request = OneTimeWorkRequestBuilder<MilestoneWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag(tagForHabit(habitId))
                    .build()

                workManager.enqueue(request)
            }
        }
    }

    fun cancelMilestonesForHabit(habitId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tagForHabit(habitId))
    }

    private fun tagForHabit(habitId: Int) = "milestone_habit_$habitId"
}
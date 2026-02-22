package com.example.nicht_raucher_app.util

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.SubstanceType
import com.example.nicht_raucher_app.milestones.Milestone
import com.example.nicht_raucher_app.milestones.MilestoneData

data class MilestoneProgress(
    val nextMilestone: Milestone?,
    val lastReachedMilestone: Milestone?,
    val progress: Float,          // 0.0 – 1.0 zum nächsten Milestone
    val timeSavedMinutes: Long    // gewonnene Lebenszeit in Minuten
)

object MilestoneUtils {

    private fun minutesPerUnit(type: SubstanceType): Double = when (type) {
        SubstanceType.CIGARETTES,
        SubstanceType.TOBACCO       -> 5.0
        SubstanceType.ALCOHOL       -> 20.0
        SubstanceType.CANNABIS      -> 15.0
        SubstanceType.COFFEE        -> 10.0
        SubstanceType.SUGAR,
        SubstanceType.ENERGY_DRINKS -> 5.0
        else                        -> 10.0
    }

    fun calculateProgress(habit: Habit): MilestoneProgress {
        val substanceType = try {
            SubstanceType.valueOf(habit.substanceType)
        } catch (e: IllegalArgumentException) {
            SubstanceType.CUSTOM
        }

        val milestones = MilestoneData.getMilestones(substanceType)
        val elapsed = System.currentTimeMillis() - habit.startTimeMillis
        val elapsedDays = elapsed / 86_400_000.0

        val lastReached = milestones.lastOrNull { it.durationMillis <= elapsed }
        val next = milestones.firstOrNull { it.durationMillis > elapsed }

        val progress = if (next == null) {
            1f
        } else {
            val previousMillis = lastReached?.durationMillis ?: 0L
            val range = next.durationMillis - previousMillis
            val done = elapsed - previousMillis
            (done.toFloat() / range.toFloat()).coerceIn(0f, 1f)
        }

        val timeSaved = (habit.unitsPerDay * minutesPerUnit(substanceType) * elapsedDays).toLong()

        return MilestoneProgress(
            nextMilestone = next,
            lastReachedMilestone = lastReached,
            progress = progress,
            timeSavedMinutes = timeSaved
        )
    }

    // Rückwärtskompatibilität für bestehende Aufrufe
    fun getNextMilestone(startTimeMillis: Long) =
        MilestoneData.milestones.firstOrNull {
            it.durationMillis > System.currentTimeMillis() - startTimeMillis
        }

    fun getProgressToNext(startTimeMillis: Long): Float {
        val elapsed = System.currentTimeMillis() - startTimeMillis
        val milestones = MilestoneData.milestones
        val nextIndex = milestones.indexOfFirst { it.durationMillis > elapsed }
        if (nextIndex < 0) return 1f
        val prevDuration = if (nextIndex == 0) 0L else milestones[nextIndex - 1].durationMillis
        val nextDuration = milestones[nextIndex].durationMillis
        return ((elapsed - prevDuration).toFloat() / (nextDuration - prevDuration).toFloat())
            .coerceIn(0f, 1f)
    }
}
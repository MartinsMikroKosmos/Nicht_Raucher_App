package com.example.nicht_raucher_app.util

import java.time.Duration

object TimeUtils {

    data class TimeDisplay(
        val days: Long,
        val hours: Long,
        val minutes: Long,
        val seconds: Long
    )

    /**
     * Berechnet die verstrichene Zeit seit einem Startzeitpunkt.
     */
    fun getElapsedDuration(startTimeMillis: Long): TimeDisplay {
        val now = System.currentTimeMillis()
        val duration = Duration.ofMillis(now - startTimeMillis)

        return TimeDisplay(
            days = duration.toDays(),
            hours = duration.toHours() % 24,
            minutes = duration.toMinutes() % 60,
            seconds = duration.seconds % 60
        )
    }

    /**
     * Formatiert die Dauer als String, z.B. "2 Tage, 5 Std."
     */
    fun formatDuration(display: TimeDisplay): String {
        return "${display.days}d ${display.hours}h ${display.minutes}m ${display.seconds}s"
    }
}
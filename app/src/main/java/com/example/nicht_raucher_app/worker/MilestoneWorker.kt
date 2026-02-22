package com.example.nicht_raucher_app.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nicht_raucher_app.util.AppConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MilestoneWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val habitId = inputData.getInt(KEY_HABIT_ID, -1)
        val habitLabel = inputData.getString(KEY_HABIT_LABEL) ?: return Result.failure()
        val title = inputData.getString(KEY_MILESTONE_TITLE) ?: return Result.failure()
        val body = inputData.getString(KEY_MILESTONE_BODY) ?: return Result.failure()

        if (habitId == -1) return Result.failure()

        showNotification(habitId, habitLabel, title, body)
        return Result.success()
    }

    private fun showNotification(notifId: Int, habitLabel: String, title: String, body: String) {
        val hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return

        val notification = NotificationCompat.Builder(context, AppConfig.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("$habitLabel – $title")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notifId, notification)
    }

    companion object {
        const val KEY_HABIT_ID = "habit_id"
        const val KEY_HABIT_LABEL = "habit_label"
        const val KEY_MILESTONE_TITLE = "milestone_title"
        const val KEY_MILESTONE_BODY = "milestone_body"
    }
}
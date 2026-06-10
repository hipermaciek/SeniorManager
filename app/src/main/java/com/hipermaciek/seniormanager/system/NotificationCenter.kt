package com.hipermaciek.seniormanager.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationCenter {
    private const val CHANNEL_LOW = "reminder"
    private const val CHANNEL_MEDIUM = "missed_dose"
    private const val CHANNEL_HIGH = "low_stock"
    private const val CHANNEL_CRITICAL = "critical"

    fun ensureChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        listOf(
            NotificationChannel(CHANNEL_LOW, "Reminders", NotificationManager.IMPORTANCE_DEFAULT),
            NotificationChannel(CHANNEL_MEDIUM, "Missed doses", NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_HIGH, "Low stock", NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_CRITICAL, "Critical", NotificationManager.IMPORTANCE_MAX)
        ).forEach(manager::createNotificationChannel)
    }

    fun notify(context: Context, id: Int, title: String, body: String, priority: String) {
        ensureChannels(context)
        val channel = when (priority) {
            "LOW" -> CHANNEL_LOW
            "MEDIUM" -> CHANNEL_MEDIUM
            "HIGH" -> CHANNEL_HIGH
            else -> CHANNEL_CRITICAL
        }
        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

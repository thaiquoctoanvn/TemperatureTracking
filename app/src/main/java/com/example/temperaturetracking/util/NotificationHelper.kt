package com.example.temperaturetracking.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.temperaturetracking.R

class NotificationHelper(private val context: Context, private val notificationManager: NotificationManager) {

    fun showNotificationMessage(id: Int, message: String, intent: Intent) {
        if (TextUtils.isEmpty(message)) {
            return
        }

        val notificationIcon =
            R.drawable.ic_app
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
            context.applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ConstantHelper.CHANNEL_ID,
                ConstantHelper.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    ConstantHelper.CHANNEL_DECRYPTION
            }
            notificationManager.createNotificationChannel(channel)
        }

        createNotification(notificationManager, notificationIcon, message, resultPendingIntent)
    }

    private fun createNotification(
        notificationManager: NotificationManager,
        icon: Int,
        message: String,
        pendingIntent: PendingIntent
    ) {
        val notificationBuilder = NotificationCompat.Builder(
            context,
            ConstantHelper.CHANNEL_ID
        )
            .setSmallIcon(icon)
            .setContentTitle(ConstantHelper.NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(ConstantHelper.NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun wakeUpScreen() {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isInteractive) {

        }
    }
}
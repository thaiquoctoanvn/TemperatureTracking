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
import kotlin.random.Random

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
            .setContentTitle("${ConstantHelper.NOTIFICATION_TITLE} | $message")
            .setContentText(generateMessageNotification(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(Random.nextInt(10, 999), notificationBuilder.build())
    }

    fun wakeUpScreen() {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if(!powerManager.isInteractive) {
            // Tag mặc định nên là "myApp:notificationLock"
            val wakeLock = powerManager.
            newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock")
            wakeLock.acquire(5000)
        }
    }

    private fun generateMessageNotification(temperature: String): String {
        return when (temperature.toFloat()) {
            in 0.0..19.9 -> {
                context.getString(R.string.alert_lv_2)
            }
            in 20.0..28.0 -> {
                context.getString(R.string.alert_lv_1)
            }
            in 29.0..37.5 -> {
                context.getString(R.string.alert_lv0)
            }
            in 37.6..39.0 -> {
                context.getString(R.string.alert_lv1)
            }
            else -> {
                context.getString(R.string.alert_lv2)
            }
        }
    }
}
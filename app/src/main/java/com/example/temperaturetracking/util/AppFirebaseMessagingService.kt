package com.example.temperaturetracking.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.example.temperaturetracking.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class AppFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        storeTokenToPre(token)
    }

    // LẮNG NGHE THÔNG BÁO TỪ SERVER
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message != null) {
            handleNotification(message)
        }
    }

    private fun storeTokenToPre(token: String) {
        val sharedPre = applicationContext.getSharedPreferences(ConstantHelper.SHARED_PREF, 0)
        with(sharedPre.edit()) {
            putString(ConstantHelper.SHARED_PREF_TOKEN, token)
            commit()
        }
    }

    private fun handleNotification(message: RemoteMessage) {
        if(!this::notificationManager.isInitialized) {
            notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        if(!this::notificationHelper.isInitialized) {
            notificationHelper = NotificationHelper(applicationContext, notificationManager)
        }

        val param: Map<String, String> = message.data
        val data = JSONObject(param)

        if(data.toString().isNotEmpty()) {
            val id = data.get("id").toString()
            val content = data.get("message").toString()


            val pushIntent = Intent(ConstantHelper.PUSH_NOTIFICATION)
            pushIntent.putExtra("id", id)
            pushIntent.putExtra("message", content)

            sendBroadcast(pushIntent)

            val actionIntent = Intent(this, MainActivity::class.java)

            notificationHelper.wakeUpScreen()
            notificationHelper.showNotificationMessage(id.toInt(), content, actionIntent)

        }
    }
}
package com.news.reader.app.service.firebaseImpl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.news.reader.app.R

const val channelId = "app_notification_channel_id"
const val channelName = "com.news.reader.app"

class FirebaseMessageService : FirebaseMessagingService() {
    private val notificationId = 1

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if the message contains data payload
        remoteMessage.data.isNotEmpty().let {
            // Extract data from the payload
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            // Handle the received data
            title?.let { articleTitle ->
                message?.let { articleMessage ->
                    // Display notification
                    displayNotification(articleTitle, articleMessage)
                }
            }
            Log.d("FCM", "Title: $title, Message: $message")
        }
    }

    private fun displayNotification(title: String, message: String) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        var notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.news_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000, 1000, 1000
                )
            )

        notificationBuilder = notificationBuilder.setContent(getRemoteView(title, message))
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
    override fun onNewToken(token: String) {
        Log.d("FCM Token", token)
    }

    private fun getRemoteView (title: String, message: String): RemoteViews{
        val remoteView = RemoteViews("com.news.reader.app", R.layout.fcm_notification)
        remoteView.setTextViewText(R.id.notification_title, title)
        remoteView.setTextViewText(R.id.notification_description, message)
        remoteView.setImageViewResource(R.id.notification_icon, R.drawable.news_icon)

        return remoteView
    }
}
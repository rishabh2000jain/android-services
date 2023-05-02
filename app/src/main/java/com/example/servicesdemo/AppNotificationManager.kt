package com.example.servicesdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat


class AppNotificationManager constructor(private val context: Context){
    private var mNotificationManager:NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification():Notification{
        return NotificationCompat.Builder(context,"122")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle("Worker running")
            .setContentText("Worker ...... Hello World!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

    }

    fun registerNotificationChannelChannel(
        channelId: String?,
        channelName: String?,
        channelDescription: String?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = channelDescription
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
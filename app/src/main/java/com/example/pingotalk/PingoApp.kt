package com.example.pingotalk

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.pingotalk.Services.notificationServices.Companion.CHANNELID
import com.example.pingotalk.Services.notificationServices.Companion.CHANNELNAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PingoApp():Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNELID,
                CHANNELNAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
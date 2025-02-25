package com.example.pingotalk.Services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.pingotalk.Model.Notification
import com.example.pingotalk.Repo.PingoRepoImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject

class notificationServices @Inject constructor( val pingoRepoImpl: PingoRepoImpl):FirebaseMessagingService() {
    companion object{
       val  CHANNELID ="Pingo_Notification"
        val CHANNELNAME = "Chat Notification"
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            pingoRepoImpl.updateToken(token)
        }

    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message?.let {
        val title = it.notification!!.title
            val description = it.notification!!.body
            notificationLaunch(Notification("",title!!,description!!))
        }
    }
    private fun notificationLaunch(data: Notification) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNELID).setContentText(data.title).setContentText(data.message).setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(0,notification.build())
    }

}
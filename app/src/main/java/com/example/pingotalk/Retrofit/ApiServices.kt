package com.example.pingotalk.Retrofit

import com.example.pingotalk.Model.Notification
import com.example.pingotalk.Model.NotificationResponse
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
interface ApiServices {
    @Headers("Content-Type: application/json")
    @POST("/send-notification")
    fun SendNotification(@Body notification: Notification): Call<NotificationResponse>
}

package com.example.pingotalk.Data_Layer.Retrofit

import com.example.pingotalk.Data_Layer.Model.Notification
import com.example.pingotalk.Data_Layer.Model.NotificationResponse
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

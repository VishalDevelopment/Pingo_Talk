package com.example.pingotalk.Data_Layer.Model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("token")
    val deviceToken:String,
    @SerializedName("title")
    val title:String ,
    @SerializedName("body")
    val message:String
)


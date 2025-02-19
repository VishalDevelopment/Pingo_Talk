package com.example.pingotalk.Model

data class User(
    val id :String ?=null,
    val name:String?=null,
    val photoUrl :String?=null,
    val email:String?=null,
    val phoneNo:String?=null,
    val subscription:String?=null,
    val FCM:String? = null
)

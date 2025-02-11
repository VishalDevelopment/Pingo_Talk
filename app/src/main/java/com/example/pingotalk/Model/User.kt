package com.example.pingotalk.Model

data class User(
    val id :String ?="Loading...",
    val name:String?=null,
    val photoUrl :String?=null,
    val email:String?=null,
    val phoneNo:String?=null,
    val subscription:String?=null
)

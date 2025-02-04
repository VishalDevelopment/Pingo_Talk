package com.example.pingotalk.Model

data class User(
    val id :String ?=null,
    val name:String?=null,
    val photoUrl :String?=null,
    val email:String?=null,
    val country:String?=null,
    val subscription:String?=null
)

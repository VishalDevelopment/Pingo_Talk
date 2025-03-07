package com.example.pingotalk.Common.Routes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.pingotalk.Data_Layer.Model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val UserData = object : NavType <User>(isNullableAllowed = false){
        override fun get(bundle: Bundle, key: String): User? {
            return Json.decodeFromString(bundle.getString(key)?:return null)
        }

        override fun parseValue(value: String): User {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: User): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: User) {
            bundle.putString(key,Json.encodeToString(value))
        }

    }
}
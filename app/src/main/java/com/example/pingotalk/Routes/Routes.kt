package com.example.pingotalk.Routes

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object SiginInScreen : Routes()

    @Serializable
    object HomeScreen : Routes()

    @Serializable
    object ChatScreen : Routes()

    @Serializable
    object ProfileScreen : Routes()

    @Serializable
    object SearchScreen:Routes()
}
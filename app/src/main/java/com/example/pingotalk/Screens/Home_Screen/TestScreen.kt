package com.example.pingotalk.Screens.Home_Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.pingotalk.Model.User
import com.example.pingotalk.Screens.Home_Screen.viewmodel.ChatViewModel

@Composable
fun TestScreen(user: User?, chatViewModel: ChatViewModel) {



    val fontSize = 22.sp
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "ID: ${user?.id ?: "Loading..."}", fontSize = fontSize)
        Text(text = "Name: ${user?.name ?: "Unknown"}", fontSize = fontSize)
        Text(text = "Photo URL: ${user?.photoUrl ?: "No Photo"}", fontSize = fontSize)
        Text(text = "Email: ${user?.email ?: "No Email"}", fontSize = fontSize)
    }
}
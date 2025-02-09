package com.example.pingotalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.Screens.Chat_Screen.ChatScreen
import com.example.pingotalk.Screens.Home_Screen.HomeScreen
import com.example.pingotalk.Screens.Landing_Screen.SignInScreen
import com.example.pingotalk.ui.theme.PingoTalkTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingoTalkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        StartApp()
                    }
                }
            }
        }
    }

    @Composable
    fun StartApp() {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val startDestination = if (currentUser?.uid != null) {
            Routes.HomeScreen
        } else {
            Routes.SiginInScreen
        }

        var chatFeature = ChatData()

        NavHost(navController = navController, startDestination = startDestination) {
            composable<Routes.SiginInScreen> {
                SignInScreen(navController)
            }
            composable<Routes.HomeScreen> {

                HomeScreen(){ chat ->
                    chatFeature = chat
                    navController.navigate(Routes.ChatScreen)
                        {
                            launchSingleTop = true
                             popUpTo(Routes.HomeScreen) { inclusive = false }

                    }
                }
            }
            composable<Routes.ChatScreen>{
                ChatScreen(chatFeature) {
                    navController.navigateUp()
                }
            }
        }
    }

}


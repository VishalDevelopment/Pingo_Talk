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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.Screens.Home_Screen.HomeScreen
import com.example.pingotalk.Screens.Home_Screen.viewmodel.ChatViewModel
import com.example.pingotalk.Screens.Signin_Screen.SignInScreen
import com.example.pingotalk.Screens.Signin_Screen.viewmodel.GoogleSignInViewModel
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
        val signInViewModel: GoogleSignInViewModel = hiltViewModel()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val startDestination = if (currentUser?.uid != null) {
            Routes.HomeScreen
        } else {
            Routes.SiginInScreen
        }
//            LaunchedEffect(currentUser) {
//                if (currentUser?.uid!=null){
//                Log.d("USERID", "user Id : ${currentUser.uid}")
//                chatViewModel.getUserDetail(currentUser.uid)
//                }
//            }
//        val userData = chatViewModel.userData
//        Log.d("UserData","${userData.value}")
        val user = signInViewModel.user.collectAsState()
        NavHost(navController = navController, startDestination = startDestination) {
            composable<Routes.SiginInScreen> {
                SignInScreen(navController)
            }
            composable<Routes.HomeScreen> {
                val chatViewModel : ChatViewModel = hiltViewModel()
                LaunchedEffect (Unit){
                    chatViewModel.startListeningToUser(user.value.id.toString())
                }
//                val user_Data = chatViewModel.userData.collectAsState().value
                HomeScreen( user.value,chatViewModel)
            }
        }
    }

}


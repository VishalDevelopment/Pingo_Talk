package com.example.pingotalk

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pingotalk.Model.User
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.Screens.Home_Screen.HomeScreen
import com.example.pingotalk.Screens.Home_Screen.Home_Viewmodel.HomeViewModel
import com.example.pingotalk.Screens.Signin_Screen.SignInScreen
import com.example.pingotalk.Viewmodel.GoogleSignInViewModel
import com.example.pingotalk.ui.theme.PingoTalkTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

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
        val chatViewModel: HomeViewModel = hiltViewModel()

        // Collect user state
        val user by signInViewModel.user.collectAsState()

        // Ensure Navigation Destination updates dynamically
        val startDestination = if (user?.id != null) {
            Routes.HomeScreen
        } else {
            Routes.SiginInScreen
        }


        val userInfo = remember {
            mutableStateOf(User())
        }
        // Fetch user details when logged in
        LaunchedEffect(user?.id) {
            user?.id?.let { userId ->
                 chatViewModel.getUserDetail(userId).collectLatest {
                     userInfo.value = it
                 }
            }
        }


        Log.d("MAIN","Google Vm data :$user ")
        NavHost(navController = navController, startDestination = startDestination) {
            composable<Routes.SiginInScreen> {
                SignInScreen(navController)
            }
            composable<Routes.HomeScreen> {
                Log.d("MAIN","Google Vm data in NAv :$user ")
                Log.d("MAIN","Chat Vm data in Nav : ${userInfo.value}")
                HomeScreen(userInfo.value)
            }
        }
    }

}


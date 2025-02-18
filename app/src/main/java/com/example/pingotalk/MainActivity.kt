package com.example.pingotalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.User
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.Screens.Chat_Screen.ChatScreen
import com.example.pingotalk.Screens.Home_Screen.HomeScreen
import com.example.pingotalk.Screens.Landing_Screen.SignInScreen
import com.example.pingotalk.Screens.Profile_Screen.ProfileScreen
import com.example.pingotalk.Screens.Search_Screen.SearchScreen
import com.example.pingotalk.Viewmodel.PingoViewmodel
import com.example.pingotalk.ui.theme.PingoTalkTheme
import dagger.hilt.android.AndroidEntryPoint


var user = mutableStateOf<User?>(null)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val pingoVm: PingoViewmodel = hiltViewModel()
            installSplashScreen().apply {
                setKeepOnScreenCondition {
                    pingoVm.splash
                }
            }
            PingoTalkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        StartApp(pingoVm)
                    }
                }
            }
        }
    }

    @Composable
    fun StartApp(pingoVm: PingoViewmodel) {
        val navController = rememberNavController()
        var chatFeature = ChatData()
        var destination =pingoVm.startDestination.collectAsState().value

        LaunchedEffect(destination) {
            Log.d("PINGO","destination : $destination")
        }
        NavHost(
            navController = navController,
            startDestination = destination
        ) {
            composable<Routes.SiginInScreen> {
                SignInScreen()
            }
            composable<Routes.HomeScreen> {
                HomeScreen({ chat ->
                    chatFeature = chat
                    navController.navigate(Routes.ChatScreen) {
                        launchSingleTop = true
                        popUpTo(Routes.HomeScreen) { inclusive = false }
                    }
                }, {
                    navController.navigate(Routes.ProfileScreen)
                },{
                    navController.navigate(Routes.SearchScreen)
                }, user.value!!)
            }
            composable<Routes.ChatScreen>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                ChatScreen(chatFeature) {
                    navController.navigateUp()
                }
            }
            composable<Routes.ProfileScreen>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },  // Slide in from right
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }    // Slide out to right
            ) {
                ProfileScreen ({
                    navController.navigateUp()
                },{
                    navController.navigate(Routes.SiginInScreen){
                        popUpTo(Routes.HomeScreen){
                            inclusive =true
                        }
                    }
                }, user)
            }

            composable<Routes.SearchScreen>(
                enterTransition = {
                    scaleIn(initialScale = 0.8f) + fadeIn() // Scale up + fade in
                },
                exitTransition = {
                    scaleOut(targetScale = 0.8f) + fadeOut() // Scale down + fade out
                }
            ){
                SearchScreen()
            }
        }
    }

}


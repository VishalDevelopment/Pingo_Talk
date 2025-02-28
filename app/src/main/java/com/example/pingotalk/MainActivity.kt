package com.example.pingotalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pingotalk.Data_Layer.Model.ChatData
import com.example.pingotalk.Data_Layer.Model.User
import com.example.pingotalk.Common.Routes.Routes
import com.example.pingotalk.Ui_Layer.Screens.Chat_Screen.ChatScreen
import com.example.pingotalk.Ui_Layer.Screens.Home_Screen.HomeScreen
import com.example.pingotalk.Ui_Layer.Screens.Landing_Screen.SignInScreen
import com.example.pingotalk.Ui_Layer.Screens.Profile_Screen.ProfileScreen
import com.example.pingotalk.Ui_Layer.Screens.Search_Screen.SearchScreen
import com.example.pingotalk.Ui_Layer.Viewmodel.PingoViewmodel
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
        var destination = pingoVm.startDestination.collectAsState().value

        LaunchedEffect(destination) {
            Log.d("PINGO", "destination : $destination")
        }
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = destination,
                modifier = Modifier.matchParentSize()
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
                    }, {
                        navController.navigate(Routes.SearchScreen)
                    }, user.value!!)
                }
                composable<Routes.ChatScreen>(
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) // Slower transition
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) // Slower exit
                    }
                ) {
                    ChatScreen(chatFeature) {
                        navController.navigateUp()
                    }
                }
                composable<Routes.ProfileScreen>(
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) // Slower transition
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(durationMillis = 600)
                        ) // Slower exit
                    }
                ) {
                    ProfileScreen({
                        navController.navigateUp()
                    }, user)
                }
                composable<Routes.SearchScreen>(
                    enterTransition = {
                        scaleIn(
                            initialScale = 0.8f,
                            animationSpec = tween(durationMillis = 600)
                        ) + fadeIn(animationSpec = tween(durationMillis = 600)) // Slower transition
                    },
                    exitTransition = {
                        scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 600)
                        ) + fadeOut(animationSpec = tween(durationMillis = 600)) // Slower exit
                    }
                ) {
                    SearchScreen({ chat ->
                        chatFeature = chat
                        navController.navigate(Routes.ChatScreen) {
                            launchSingleTop = true
                            popUpTo(Routes.HomeScreen) { inclusive = false }
                        }
                    })
                }
            }
        }
    }
}

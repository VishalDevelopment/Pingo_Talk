package com.example.pingotalk.Ui_Layer.Viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pingotalk.Data_Layer.Model.User
import com.example.pingotalk.Data_Layer.Repo.PingoRepoImpl
import com.example.pingotalk.Common.Routes.Routes

import com.example.pingotalk.user
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

import kotlin.system.measureTimeMillis

@HiltViewModel
class PingoViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val pingoRepoImpl: PingoRepoImpl
) : ViewModel() {

    var splash by mutableStateOf(true)
    val startDestination = pingoRepoImpl.startDestination.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val splashTime = measureTimeMillis {  // Start measuring time
                val currentUser = firebaseAuth.currentUser

                if (currentUser == null) {
                    pingoRepoImpl.startDestination.emit(Routes.SiginInScreen)
                } else {
                    Log.d("SHOPVM", "NOT NULL : $currentUser")
                    user.value = User(
                        id = currentUser.uid ?: "",
                        name = currentUser.displayName,
                        photoUrl = currentUser.photoUrl?.toString() ?: "",
                        email = currentUser.email,
                        phoneNo = "",
                        subscription = "free"
                    )
                    pingoRepoImpl.fetchUserData()
                    pingoRepoImpl.getAllChatPartners()
                    pingoRepoImpl.startDestination.emit(Routes.HomeScreen)
                }
            }  // End measuring time

            Log.d("SplashTime", "Splash screen duration: $splashTime ms")

            withContext(Dispatchers.Main) {
                splash = false
            }
        }
    }
}

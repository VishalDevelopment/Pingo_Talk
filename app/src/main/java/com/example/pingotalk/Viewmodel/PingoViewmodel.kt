package com.example.pingotalk.Viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pingotalk.Repo.PingoRepoImpl
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.startDestination
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PingoViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val pingoRepoImpl: PingoRepoImpl
):ViewModel() {

    var splash by mutableStateOf(true)
    init {
        checkLoginStatus()
    }

    private  fun checkLoginStatus(){
        val currentUser = firebaseAuth.currentUser
        viewModelScope.launch {

            if (currentUser == null) {
                Log.d("SHOPVM","NULL : $currentUser")
                startDestination=   Routes.SiginInScreen
            } else  {
                Log.d("SHOPVM","NOT NULL : $currentUser")
                startDestination=  Routes.HomeScreen
            }
            delay(100)
            splash = false
        }

    }
}
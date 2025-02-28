package com.example.pingotalk.Ui_Layer.Screens.Profile_Screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Data_Layer.Repo.PingoRepoImpl
import com.example.pingotalk.Common.Routes.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProfileViewmodel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val pingoRepoImpl: PingoRepoImpl,
    val firebaseMessaging: FirebaseMessaging,
) : ViewModel() {

    fun SignoutApp() {
        viewModelScope.launch {
            try {
                firebaseMessaging.deleteToken().await()
                pingoRepoImpl.startDestination.emit(Routes.SiginInScreen)
                firebaseAuth.signOut()
            } catch (e: Exception) {
                Log.e("SignOutError", "Error signing out: ${e.message}")
            }
        }
    }

}
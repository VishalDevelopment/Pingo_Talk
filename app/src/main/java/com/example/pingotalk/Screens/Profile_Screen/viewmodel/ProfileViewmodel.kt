package com.example.pingotalk.Screens.Profile_Screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Repo.PingoRepoImpl
import com.example.pingotalk.Routes.Routes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewmodel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val pingoRepoImpl: PingoRepoImpl
):ViewModel() {

    fun SignoutApp(){
        viewModelScope.launch {
        pingoRepoImpl.startDestination.emit(Routes.SiginInScreen)
        firebaseAuth.signOut()
        }
    }

}
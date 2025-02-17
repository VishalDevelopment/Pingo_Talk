package com.example.pingotalk.Screens.Profile_Screen.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewmodel @Inject constructor(
    val firebaseAuth: FirebaseAuth
):ViewModel() {

    fun SignoutApp(){
        firebaseAuth.signOut()
    }

}
package com.example.pingotalk.Screens.Landing_Screen.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Repo.PingoRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(private val pingoRepo: PingoRepoImpl) : ViewModel() {
    val siginLoading = MutableStateFlow(false)



    fun HandleGoogleSigning(context: Context){
        viewModelScope.launch {
            siginLoading.value = true
            pingoRepo.handleGoogleSignIn(context) {
                data ->
                siginLoading.value = data
            }
        }
    }
}
package com.example.pingotalk.Screens.Landing_Screen.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.pingotalk.Model.User
import com.example.pingotalk.R
import com.example.pingotalk.Repo.PingoRepo
import com.example.pingotalk.Repo.PingoRepoImpl
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.State
import com.example.pingotalk.startDestination
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(private val pingoRepo: PingoRepoImpl) : ViewModel() {

    fun HandleGoogleSigning(context: Context){
        viewModelScope.launch {
            pingoRepo.handleGoogleSignIn(context)
        }
    }
}
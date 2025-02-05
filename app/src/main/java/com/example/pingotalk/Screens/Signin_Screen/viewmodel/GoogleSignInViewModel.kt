package com.example.pingotalk.Screens.Signin_Screen.viewmodel

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
import com.example.pingotalk.Routes.Routes
import com.example.pingotalk.State
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
class GoogleSignInViewModel @Inject constructor(val firebaseAuth: FirebaseAuth,val firebaseStore: FirebaseFirestore) : ViewModel() {


    val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _user.value = User(
                id = currentUser.uid,
                name = currentUser.displayName ?: "Unknown",
                photoUrl = currentUser.photoUrl?.toString() ?: "",
                email = currentUser.email ?: "No Email",
                country = "",
                subscription = "Free"
            )
        }
    }

    fun handleGoogleSignIn(context: Context,navController: NavController) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                when (result) {
                    is State.Success -> {
                        val currentUser = result.data!!.user
                        if (currentUser!=null){
                                _user.value = User(
                                    id = currentUser.uid,
                                    name = currentUser.displayName ?: "Unknown",
                                    photoUrl = currentUser.photoUrl?.toString() ?: "",
                                    email = currentUser.email ?: "No Email",
                                    country = "",
                                    subscription = "Free"
                                )

                            Toast.makeText(context, "Account Created Successfully !", Toast.LENGTH_SHORT).show()


                            val userCollection = firebaseStore.collection("USERS").document(currentUser.uid)
                            userCollection.get().addOnSuccessListener {
                                if (it.exists()){
                                    userCollection.set(_user.value!!, SetOptions.merge())
                                }
                                else{
                                    userCollection.set(_user.value!!)
                                }
                            }.addOnFailureListener {

                            }

                            navController.navigate(Routes.HomeScreen){
                                popUpTo(Routes.SiginInScreen) {
                                    inclusive = true
                                } // Removes Screen A from the back stack
                            }
                        }
                        else{
                            val message = result.message.toString()
                            Toast.makeText(context, "something went wrong : $message!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    is State.Error -> {
                        val message = result.message
                        Toast.makeText(context, "something went wrong : $message!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is State.Loading -> {
                        // Show loading UI if needed
                    }
                }
            }
        }
    }

    private suspend fun googleSignIn(context: Context): Flow<State<AuthResult>> {

        return callbackFlow {
            try {
                val credentialManager = CredentialManager.create(context)
                //Generate Nonce for security
                val ranNonce = UUID.randomUUID().toString()
                val byte = ranNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(byte)
                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.webclient))
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    trySend(State.Success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type. ")
                }

            } catch (e: GetCredentialCancellationException) {
                trySend(State.Error(e.message.toString()))
            } catch (e: Exception) {
                trySend(State.Error(e.message.toString()))
            }
            awaitClose()
        }
    }
}
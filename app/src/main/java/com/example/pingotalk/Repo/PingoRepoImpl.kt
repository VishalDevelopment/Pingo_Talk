package com.example.pingotalk.Repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.pingotalk.Model.User
import com.example.pingotalk.R
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject


class PingoRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore,
) : PingoRepo {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()
    override suspend fun handleGoogleSignIn(context: Context, ChangeState: (Boolean) -> Unit) {
        ChangeState(true) // Set loading state before making API call

        googleSignIn(context).collect { result ->
            when (result) {
                is State.Success -> {
                    val currentUser = result.data!!.user
                    if (currentUser != null) {
                        withContext(Dispatchers.IO) {
                            val user = User(
                                id = firebaseAuth.currentUser?.uid ?: "",
                                name = currentUser.displayName ?: "Unknown",
                                photoUrl = currentUser.photoUrl?.toString() ?: "",
                                email = currentUser.email ?: "No Email",
                                phoneNo = "",
                                subscription = "free"
                            )

                            val userCollection = firebaseStore.collection("USERS").document(currentUser.uid)
                            val userExists = userCollection.get().await().exists()

                            if (userExists) {
                                userCollection.set(user, SetOptions.merge()).await()
                            } else {
                                userCollection.set(user).await()
                            }

                            withContext(Dispatchers.Main) {
                                com.example.pingotalk.user.value = user
                                startDestination = Routes.HomeScreen
                                Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                                ChangeState(false) // Set loading state to false after success
                            }
                        }
                    } else {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                        ChangeState(false) // Ensure loading stops on error
                    }
                }

                is State.Error -> {
                    Toast.makeText(context, "Something went wrong: ${result.message}!!", Toast.LENGTH_SHORT).show()
                    ChangeState(false) // Stop loading on error
                }

                is State.Loading -> {
                    Log.d("GOOGLE", "TRUE")
                    ChangeState(true) // Ensure loading is set to true here
                }
            }
        }
    }
    override suspend fun googleSignIn(context: Context): Flow<State<AuthResult>> {

        return callbackFlow {
            trySend(State.Loading())
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
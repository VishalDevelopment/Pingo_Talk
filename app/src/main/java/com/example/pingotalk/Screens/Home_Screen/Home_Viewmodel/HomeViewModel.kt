package com.example.pingotalk.Screens.Home_Screen.Home_Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pingotalk.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    fun getUserDetail(currentUserId: String): Flow<User> = flow {

        val userData = MutableStateFlow(User())
        firestore.collection("USERS").document(currentUserId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        val user =
                            document.toObject(User::class.java) // Convert document to User data class
                        if (user != null) {
                            userData.value = User(
                                user.id!!,
                                user.name!!,
                                user.photoUrl!!,
                                user!!.email,
                                user!!.country,
                                user.subscription
                            )
                        }

                    } catch (e: Exception) {
                        Log.d("VM", "Exception : ${e.message}")
                    }// Pass the result to the callback function

                } else {
                    // Document does not exist
                    Log.d("VM", "Document : not Exist")
                }
            }
            .addOnFailureListener {
                // Handle failure

            }
        emit(userData.value)
    }

}
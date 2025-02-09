package com.example.pingotalk.Screens.Home_Screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.ChatUser
import com.example.pingotalk.Model.Message
import com.example.pingotalk.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _userData = MutableStateFlow<User>(User())
    val userData = _userData.asStateFlow()
    private var userDataListener: ListenerRegistration? = null

    fun fetchUserData() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            userDataListener = firestore.collection("USERS")
                .document(currentUserId)
                .addSnapshotListener(MetadataChanges.INCLUDE) { document, error ->

                    document?.let {
                        val user = document.toObject(User::class.java)
                        if (document.exists() && user != null) {
                            _userData.value = user
                            Log.d("VM", "User data updated: $user")
                        } else {
                            Log.d("VM", "Document does not exist")
                        }
                    }
                    error?.let {
                        Log.d("VM", "Error fetching user data: ${error.message}")
                    }
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        userDataListener?.remove()
        chatListener?.remove()
    }

    private val _chat = MutableStateFlow<List<ChatData>>(emptyList())
    val chat = _chat.asStateFlow()
    private var chatListener: ListenerRegistration? = null

    fun getAllChatPartners() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            chatListener = firestore.collection("CHATS")
                .document(currentUserId)
                .collection("chats")
                .addSnapshotListener { result, error ->

                    if (error != null) {
                        Log.w("Chats", "Error getting chats for user $currentUserId", error)
                        return@addSnapshotListener
                    }

                    result?.let {
                        val chatList = it.toObjects(ChatData::class.java)
                        viewModelScope.launch {
                            _chat.emit(chatList)
                        }
                        Log.d("Chats", "Found ${chatList.size} chats for user $currentUserId")
                    } ?: run {
                        Log.d("Chats", "No chats found for user $currentUserId")
                    }
                }
        }
    }

    fun addChatPartner(email: String) {
        firestore.collection("CHATS").where(
            Filter.or(
                Filter.and(
                    Filter.equalTo("user1.email", email),
                    Filter.equalTo("user2.email", userData.value?.email)
                ),
                Filter.equalTo("user2.email", email),
                Filter.equalTo("user1.email", userData.value?.email)
            )
        ).get().addOnSuccessListener {

            if (it.isEmpty) {
                firestore.collection("USERS").whereEqualTo("email", email).get()
                    .addOnSuccessListener { userSnapshot ->
                        if (userSnapshot.isEmpty) {
                            Log.d("AddChat", "User not exist on USER Firestore")
                        }
                        else {
                            val chatPartner = userSnapshot.toObjects(User::class.java).firstOrNull()
                            val id = firestore.collection("CHATS").document()
                            val chat = ChatData(
                                chatId = id.id,
                                last = Message(senderId = "", content = "", time =""),
                                user1 = ChatUser(
                                    userId = userData.value!!.id.toString(),
                                    typing = false,
                                    bio = "",
                                    username = userData.value!!.name.toString(),
                                    ppurl = userData.value!!.photoUrl.toString(),
                                    email = userData.value!!.email.toString(),
                                ),
                                user2 = ChatUser(
                                    userId = chatPartner?.id ?: "",
                                    typing = false,
                                    bio = "",
                                    username = chatPartner?.name ?: "",
                                    ppurl = chatPartner?.photoUrl ?: "",
                                    email = chatPartner?.email ?: "",
                                    status = false,
                                    unread = 0
                                )
                            )

                        }
                    }
            }


        }
    }

}
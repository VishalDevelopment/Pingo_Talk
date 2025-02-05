package com.example.pingotalk.Screens.Home_Screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.ChatUser
import com.example.pingotalk.Model.Message
import com.example.pingotalk.Model.User
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()
    private var userDataListener: ListenerRegistration? = null

    val chat = MutableStateFlow<List<ChatData>>(emptyList())

    fun startListeningToUser(currentUserId: String) {
        userDataListener = firestore.collection("USERS")
            .document(currentUserId)
            .addSnapshotListener(MetadataChanges.INCLUDE) { document, error ->
                if (error != null) {
                    Log.d("VM", "Error fetching user data: ${error.message}")
                    return@addSnapshotListener
                }

                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        _userData.value = user
                        Log.d("VM", "User data updated: $user")
                    }
                } else {
                    Log.d("VM", "Document does not exist")
                }
            }
    }

    fun addChat(email: String) {
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
                firestore.collection("USERS").whereEqualTo("email", email).get().addOnSuccessListener {

                    if (it.isEmpty) {
                        Log.d("", "User not exist on USER fireStore")
                    } else {
                        val chatPartner = it.toObjects((User::class.java)).firstOrNull()
                        val id = firestore.collection("CHAT").document()
                        val chat = ChatData(
                            chatId = userData.value!!.id,
                            last = Message(
                                senderId = "",
                                content = "",
                                time = null,

                                ),
                            user1 = ChatUser(
                                userId = userData.value!!.id.toString(),
                                typing = false,
                                bio = "",
                                username = userData.value!!.name.toString(),
                                ppurl = userData.value!!.photoUrl.toString(),
                                email = userData.value!!.email.toString(),
                            ),
                            user2 = ChatUser(
                                userId = chatPartner?.id ?:"",
                                typing = false,
                                bio = "",
                                username = chatPartner?.name?:"",
                                ppurl = chatPartner?.photoUrl?:"",
                                email = chatPartner?.email?:"",
                                status = false,
                                unread = 0
                            )
                        )

                        firestore.collection("CHAT").document(userData.value?.id!!).set(chat)

                    }
                }
            }
        }
    }
    fun showChats(chatId:String){

    }
}
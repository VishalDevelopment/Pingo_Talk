package com.example.pingotalk.Screens.Chat_Screen.viewmodel

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.ChatUser
import com.example.pingotalk.Model.Message
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObjects
import com.google.protobuf.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    private val userId = firebaseAuth.currentUser!!.uid




    private val _individialchat = MutableStateFlow<List<Message>>(emptyList())
    val individualchat = _individialchat.asStateFlow()
    var individualchatListener: ListenerRegistration? = null
    fun receiveMessages(chatId:String) {
//        val currentUserId = firebaseAuth.currentUser?.uid

           individualchatListener = firestore.collection("MESSAGE")
                .document(chatId)
                .collection("message")
                .addSnapshotListener { result, error ->

                    if (error != null) {
                        Log.w("MESSAGE", "Error getting chats for user , error")
                        return@addSnapshotListener
                    }

                    result?.let {
                        val messageList = it.toObjects(Message::class.java)
                        viewModelScope.launch {
                           _individialchat.emit(messageList)
                        }
                        Log.d("MESSAGE", "Found ${messageList.size}} chatId $chatId")
                    } ?: run {
                        Log.d("MESSAGE","result : null")
                    }
                }

    }
    fun sendMessage(chatId: ChatData, message: String) {
        val messageId = EncryptedCode()
        val time = calculateTime()

        val messageData = Message(
            msgId = messageId,
            senderId = userId,
            time = time,
            content = message
        )

        firestore.collection("MESSAGE").document(chatId.chatId.toString()).collection("message").document(messageId).set(messageData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        firestore.collection("CHATS").document(userId).collection("chats")
                            .document(chatId.chatId.toString())
                            .update("last", messageData)
                        Log.d("MESSAGE", "message sent : ")
                    } catch (e: Exception) {
                        Log.d("MESSAGE", "exception : ${e.message}")
                    }
                } else {
                    Log.d("MESSAGE", "message sent : ")
                }

            }
    }





}

fun calculateTime(): String {
    val time = LocalTime.now() // Get current time
    val formatter = DateTimeFormatter.ofPattern("hh:mm a") // 12-hour format with AM/PM
    val formattedTime = time.format(formatter)
    return formattedTime.toString()

}

fun EncryptedCode(): String {
    val messageId = UUID.randomUUID().toString()
    return messageId
}
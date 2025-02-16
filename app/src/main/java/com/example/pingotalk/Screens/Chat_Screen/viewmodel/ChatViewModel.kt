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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    private val userId = firebaseAuth.currentUser!!.uid




    private val _individualChat = MutableStateFlow<List<Message>>(emptyList())
    val individualChat = _individualChat.asStateFlow()
    var individualChatListener: ListenerRegistration? = null

    fun receiveMessages(chatId: String) {
        individualChatListener = firestore.collection("MESSAGE")
            .document(chatId)
            .collection("message")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    Log.w("MESSAGE", "Error getting chats: ${error.message}")
                    return@addSnapshotListener
                }

                result?.let {
                    val messageList = it.toObjects(Message::class.java)
                        .sortedBy { it.time } // Sorting in ascending order (Old → New)

                    // No need to manually format `time`, as `Message` class handles it
                    viewModelScope.launch {
                        _individualChat.emit(messageList) // Emit messages as they are
                    }

                    // Logging the received messages
                    messageList.forEach { message ->
                        Log.d("MESSAGE", "Message: ${message.content}, Time: ${message.timeFormatted}")
                    }

                    Log.d("MESSAGE", "Found ${messageList.size} messages for chatId: $chatId")
                } ?: run {
                    Log.d("MESSAGE", "Firestore result is null")
                }
            }
    }


    // Function to format timestamp
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }



    fun sendMessage(chatId: ChatData, message: String) {
        val messageId = EncryptedCode()
        val time = System.currentTimeMillis()

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
                        firestore.collection("CHATS").document(chatId.user2.userId).collection("chats")
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


fun EncryptedCode(): String {
    val messageId = UUID.randomUUID().toString()
    return messageId
}
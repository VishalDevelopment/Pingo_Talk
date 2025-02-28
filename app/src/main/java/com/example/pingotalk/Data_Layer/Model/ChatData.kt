package com.example.pingotalk.Data_Layer.Model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatData(
    val chatId: String? = null,
    val last: Message? = null,
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser(),
)

data class Message(
    val msgId: String = "",
    val senderId: String = "",
    val repliedMsg: Message? = null,
    val reaction: List<Reaction> = emptyList(),
    val imageUrl: String = "",
    val fileUrl: String = "",
    val fileSize: String = "",
    val vidUrl: String = "",
    val progress: String = "",
    val content: String = "",
    val time: Long = 0, // Stored as Long in Firestore
    val forwarded: Boolean = false
) {
    // Computed property to format the timestamp into "hh:mm a"
    val timeFormatted: String
        get() = formatTimestamp(time)
}

// Function to format timestamp
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


data class Reaction(
    val userId: String = "",
    val ppurl: String = "",
    val username: String = "",
    val reaction: String = "",
)

data class ChatUser(
    val userId: String = "",
    val typing: Boolean = false,
    val bio: String = "",
    val username: String? = "",
    val ppurl: String = "",
    val email: String = "",
    val status: Boolean = false,
    val unread: Int = 0,
    val fcm :String =""
)
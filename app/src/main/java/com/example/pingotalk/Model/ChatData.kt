package com.example.pingotalk.Model

import com.google.protobuf.Timestamp

data class ChatData(
    val chatId: String? = null,
    val last: Message? = null,
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser=ChatUser(),
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
    val time: String = "",
    val forwarded: Boolean = false,
)

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
)
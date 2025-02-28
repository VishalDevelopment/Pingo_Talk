package com.example.pingotalk.Data_Layer.Repo

import android.content.Context
import com.example.pingotalk.Data_Layer.Model.ChatData
import com.example.pingotalk.Common.State
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface PingoRepo {

    suspend fun updateToken(NewToken:String?=null)

    suspend fun handleGoogleSignIn(context:  Context, ChangeState: (Boolean) -> Unit)
     suspend  fun googleSignIn(context: Context): Flow<State<AuthResult>>

    fun addChatPartner(email: String)
     fun  getAllChatPartners()
     fun fetchUserData()

    fun sendMessage(chatId: ChatData, message: String)
    fun  receiveMessages(chatId: String)
}
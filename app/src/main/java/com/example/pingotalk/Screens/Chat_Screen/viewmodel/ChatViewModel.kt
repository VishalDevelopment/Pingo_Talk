package com.example.pingotalk.Screens.Chat_Screen.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(val firestore: FirebaseFirestore) :ViewModel(){
    fun getSpecificChat(chatPartner:String){


    }

}
package com.example.pingotalk.Ui_Layer.Screens.Home_Screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingotalk.Data_Layer.Model.ChatData
import com.example.pingotalk.Data_Layer.Model.ChatUser
import com.example.pingotalk.Data_Layer.Model.Message
import com.example.pingotalk.Data_Layer.Model.User
import com.example.pingotalk.Data_Layer.Repo.PingoRepoImpl
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
    private val repoImpl: PingoRepoImpl
) : ViewModel() {
    val _chat = repoImpl.chat


    fun addPartner(email:String){
        viewModelScope.launch {
            repoImpl.addChatPartner(email)
        }
    }







}
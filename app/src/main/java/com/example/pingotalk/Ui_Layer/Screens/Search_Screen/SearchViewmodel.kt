package com.example.pingotalk.Ui_Layer.Screens.Search_Screen

import androidx.lifecycle.ViewModel
import com.example.pingotalk.Data_Layer.Repo.PingoRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewmodel @Inject constructor(
    private val repoImpl: PingoRepoImpl
):ViewModel() {
    val chatPartner = repoImpl.chat
}
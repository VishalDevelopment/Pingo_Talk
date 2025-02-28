package com.example.pingotalk.Ui_Layer.Screens.Search_Screen


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.pingotalk.Ui_Layer.Utils.ChatBox
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pingotalk.Data_Layer.Model.ChatData
import com.example.pingotalk.Data_Layer.Model.ChatUser
import com.example.pingotalk.Data_Layer.Model.Message
import com.example.pingotalk.R
import com.example.pingotalk.Ui_Layer.Utils.SearchBarWithAnimation
import java.util.Locale

@Composable
fun SearchScreen(MoveToChatScreen: (chatId: ChatData) -> Unit) {
    val viewmodel : SearchViewmodel = hiltViewModel()
    var searchString by remember {
        mutableStateOf("")
    }
    val ChatPartners = viewmodel.chatPartner.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.blck_blurry),
            contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBarWithAnimation { text ->
                searchString = text
            }
                val filterList:List<ChatData> = if (searchString.isEmpty()) {
                    ChatPartners.value
                } else {
                    val result = mutableListOf<ChatData>()
                    ChatPartners.value.forEach{
                        if (it.user2.username?.toLowerCase(Locale.getDefault())
                                ?.contains(searchString.toLowerCase(Locale.getDefault())) == true
                        ) {
                            result.add(ChatData(it.chatId,it.last,it.user1,it.user2))
                        }
                    }
                    result
                }
               val context = LocalContext.current
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filterList) {
                        if (filterList!=null){
                            ChatBox(chatFeature = it,MoveToChatScreen)
                        }
                    }
                }
        }
    }
}


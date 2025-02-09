package com.example.pingotalk.Screens.Chat_Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.Message
import com.example.pingotalk.R
import com.example.pingotalk.Screens.Chat_Screen.viewmodel.ChatViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ChatScreen(chatFeatures: ChatData, BackToHomeScreen: () -> Unit) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        if (chatFeatures.chatId.toString() != null) {
            if (chatFeatures.chatId.toString() != "") {
                chatViewModel.receiveMessages(chatFeatures.chatId.toString())
            } else {
                Log.d("CHATID", "chat Id is empty")
            }
        } else {
            Log.d("CHATID", "chat Id isnull")

        }
    }
    val messageList = chatViewModel.individualchat.collectAsState()

    Log.d("CHATSCREEN", "chats : ${messageList.value}")

    Scaffold(topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton(onClick = {
                BackToHomeScreen()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = "${chatFeatures.user2.ppurl}",
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${chatFeatures.user2.username}", // Replace with dynamic name
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (chatFeatures.user2.status == false) {
                                ""
                            } else {
                                "Online"
                            }, // Replace with dynamic status
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = {
                    // Handle three dot click here
                    
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
                }
            }
        )
    }) { it ->
        var message by remember {
            mutableStateOf("")
        }
        Box(
            Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.chat_layout),
                    contentScale = ContentScale.Crop
                )
                .padding(it)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxHeight(.90f)
                        .fillMaxWidth()
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        items(messageList.value) { message ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = if (message.senderId == chatFeatures.user1.userId) Alignment.CenterEnd else Alignment.CenterStart
                            ) {
                                Column(horizontalAlignment = Alignment.Start) { // Stack message + time
                                    Row(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 16.dp,
                                                    topEnd = 16.dp,
                                                    bottomEnd = if (message.senderId == chatFeatures.user1.userId) 0.dp else 16.dp,
                                                    bottomStart = if (message.senderId == chatFeatures.user1.userId) 16.dp else 0.dp
                                                )
                                            )
                                            .background(
                                                if (message.senderId == chatFeatures.user1.userId) Color(0xFFDCF8C6) else Color.DarkGray
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = message.content.toString(),
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Start,
                                            color = if (message.senderId == chatFeatures.user1.userId) Color.Black else Color.White,
                                            modifier = Modifier.wrapContentWidth()
                                        )
                                    }

                                    // Timestamp (ensures it's always visible)
                                    Text(
                                        text = "${message.time}", // Test value
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(top = 2.dp, start = 6.dp) // Space between message and time
                                            .align(Alignment.End) // Right-align for sent messages
                                    )
                                }
                            }
                        }
                    }

                }
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = message,
                        onValueChange = {
                            message = it
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },

                        trailingIcon = {
                            if (message != "") {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    tint = Color.Black, modifier = Modifier.clickable {

                                        chatViewModel.sendMessage(chatFeatures, message)
                                        message = ""
                                    }
                                )
                            }
                        },
                        placeholder = {
                            Text(text = "Type a message ...")
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    )
                }
            }
        }
    }
}








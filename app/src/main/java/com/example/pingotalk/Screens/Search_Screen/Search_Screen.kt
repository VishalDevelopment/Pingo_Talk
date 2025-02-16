package com.example.pingotalk.Screens.Search_Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.ChatUser
import com.example.pingotalk.Model.Message
import com.example.pingotalk.R
import com.example.pingotalk.Screens.Chat_Screen.ChatScreen
import com.example.pingotalk.Screens.Home_Screen.ChatBox
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

@Composable
fun SearchScreen() {
    var searchString by remember {
        mutableStateOf("")
    }
    val sampleChatList = listOf(
        ChatData(
            chatId = "1",
            last = Message(
                content = "Hey! How are you?",
                time = System.currentTimeMillis() - 600000
            ),
            user2 = ChatUser(
                username = "Alice",
                ppurl = "https://i.pravatar.cc/150?u=alice",
                bio = "Available",
                unread = 2
            )
        ),
        ChatData(
            chatId = "2",
            last = Message(
                content = "Let's catch up later!",
                time = System.currentTimeMillis() - 3600000
            ),
            user2 = ChatUser(
                username = "Bob",
                ppurl = "https://i.pravatar.cc/150?u=bob",
                bio = "Busy",
                unread = 5
            )
        ),
        ChatData(
            chatId = "3",
            last = Message(
                content = "Did you check the update?",
                time = System.currentTimeMillis() - 180000
            ),
            user2 = ChatUser(
                username = "Charlie",
                ppurl = "https://i.pravatar.cc/150?u=charlie",
                bio = "At work",
                unread = 0
            )
        ),
        ChatData(
            chatId = "4",
            last = Message(
                content = "See you soon! 😊",
                time = System.currentTimeMillis() - 900000
            ),
            user2 = ChatUser(
                username = "David",
                ppurl = "https://i.pravatar.cc/150?u=david",
                bio = "Offline",
                unread = 1
            )
        ),
        ChatData(
            chatId = "5",
            last = Message(
                content = "Hello! 👋",
                time = System.currentTimeMillis() - 300000
            ),
            user2 = ChatUser(
                username = "Eve",
                ppurl = "https://i.pravatar.cc/150?u=eve",
                bio = "Online",
                unread = 3
            )
        ),
        ChatData(
            chatId = "6",
            last = Message(
                content = "Call me when you're free.",
                time = System.currentTimeMillis() - 720000
            ),
            user2 = ChatUser(
                username = "Frank",
                ppurl = "https://i.pravatar.cc/150?u=frank",
                bio = "Away",
                unread = 4
            )
        ),
        ChatData(
            chatId = "7",
            last = Message(
                content = "Thanks for the help!",
                time = System.currentTimeMillis() - 60000
            ),
            user2 = ChatUser(
                username = "Grace",
                ppurl = "https://i.pravatar.cc/150?u=grace",
                bio = "Online",
                unread = 1
            )
        ),
        ChatData(
            chatId = "8",
            last = Message(
                content = "I'll be there at 5 PM.",
                time = System.currentTimeMillis() - 450000
            ),
            user2 = ChatUser(
                username = "Hannah",
                ppurl = "https://i.pravatar.cc/150?u=hannah",
                bio = "Do not disturb",
                unread = 6
            )
        ),
        ChatData(
            chatId = "9",
            last = Message(
                content = "Meeting postponed to tomorrow.",
                time = System.currentTimeMillis() - 240000
            ),
            user2 = ChatUser(
                username = "Isaac",
                ppurl = "https://i.pravatar.cc/150?u=isaac",
                bio = "Working",
                unread = 2
            )
        ),
        ChatData(
            chatId = "10",
            last = Message(
                content = "Happy Birthday! 🎉",
                time = System.currentTimeMillis() - 1800000
            ),
            user2 = ChatUser(
                username = "Julia",
                ppurl = "https://i.pravatar.cc/150?u=julia",
                bio = "Celebrating",
                unread = 0
            )
        ),
        ChatData(
            chatId = "11",
            last = Message(
                content = "Check your email.",
                time = System.currentTimeMillis() - 90000
            ),
            user2 = ChatUser(
                username = "Kevin",
                ppurl = "https://i.pravatar.cc/150?u=kevin",
                bio = "Available",
                unread = 7
            )
        ),
        ChatData(
            chatId = "12",
            last = Message(
                content = "Just landed! ✈️",
                time = System.currentTimeMillis() - 480000
            ),
            user2 = ChatUser(
                username = "Lily",
                ppurl = "https://i.pravatar.cc/150?u=lily",
                bio = "Traveling",
                unread = 3
            )
        ),
        ChatData(
            chatId = "13",
            last = Message(
                content = "Let's grab coffee ☕",
                time = System.currentTimeMillis() - 60000
            ),
            user2 = ChatUser(
                username = "Michael",
                ppurl = "https://i.pravatar.cc/150?u=michael",
                bio = "Free",
                unread = 5
            )
        ),
        ChatData(
            chatId = "14",
            last = Message(
                content = "Great job on the project! 👏",
                time = System.currentTimeMillis() - 750000
            ),
            user2 = ChatUser(
                username = "Nina",
                ppurl = "https://i.pravatar.cc/150?u=nina",
                bio = "Working",
                unread = 1
            )
        ),
        ChatData(
            chatId = "15",
            last = Message(
                content = "Movie night? 🍿",
                time = System.currentTimeMillis() - 180000
            ),
            user2 = ChatUser(
                username = "Oscar",
                ppurl = "https://i.pravatar.cc/150?u=oscar",
                bio = "Chilling",
                unread = 4
            )
        )
    )

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
            SearchBarWithAnimation() { text ->
                searchString = text
            }
            LazyColumn (Modifier.fillMaxWidth()){
                items(sampleChatList){
                    ChatBox(chatFeature = it){}
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithAnimation(SearchResult: (String) -> Unit) {
    val text = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }  // Controls expansion
    val width by animateDpAsState(targetValue = if (expanded) 320.dp else 56.dp, label = "") // Increased width
    val height = 56.dp // Increased height

    LaunchedEffect(Unit) {
        delay(300)  // Small delay after navigating
        expanded = true
    }

    LaunchedEffect(text) {
        SearchResult(text.value)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible = !expanded) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier
                    .size(56.dp) // Adjusted for better touch area
                    .clip(CircleShape)
                    .clickable { expanded = true }
                    .padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AnimatedVisibility(visible = expanded) {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier
                    .width(width) // Increased width
                    .height(height) // Increased height
                    .clip(RoundedCornerShape(28.dp)), // Rounded edges
                placeholder = { Text("Search...", color = Color(0xFFB0BEC5)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                },
                trailingIcon = {
                    IconButton(onClick = { expanded = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF1E3A8A).copy(alpha = 0.8f),
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}





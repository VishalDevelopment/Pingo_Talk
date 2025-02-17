package com.example.pingotalk.Screens.Search_Screen


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
import com.example.pingotalk.Utils.ChatBox
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.ChatUser
import com.example.pingotalk.Model.Message
import com.example.pingotalk.R
import com.example.pingotalk.Utils.SearchBarWithAnimation

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
            SearchBarWithAnimation { text ->
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


package com.example.pingotalk.Screens.Home_Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.InsertComment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pingotalk.Model.User
import com.example.pingotalk.R
import com.example.pingotalk.State
import com.example.pingotalk.ui.theme.FloatButton
import com.example.pingotalk.ui.theme.SkyBlue

@Composable
fun HomeScreen(user: User) {

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {

            },
            modifier = Modifier.padding(end = 20.dp, bottom = 25.dp), containerColor = FloatButton
        ) {
            Icon(
                imageVector = Icons.Filled.InsertComment,
                contentDescription = null,
                tint = Color.White
            )
        }
    }) { padding ->
        val scrollState = rememberScrollState()
        val lazyScrollState = rememberLazyListState()

        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.blck_blurry),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Main Column layout
            Column(modifier = Modifier.fillMaxSize()) {
                // Column 1 - Initially visible
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .let {
                            if (lazyScrollState.firstVisibleItemIndex > 0) {
                                it.height(0.dp)  // This will make the column "gone" when scrolling
                            } else {
                                it
                            }
                        }
                )  {
                    Text(text = "Hello", color = Color.White, fontSize = 15.sp)
                    Text(
                        text = "googleVm : ${user.name} ",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Row {
                        OutlinedButton(
                            onClick = {},
                            shape = CircleShape,
                            modifier = Modifier.size(70.dp),
                            border = BorderStroke(1.dp, Color.White),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                // Card containing the chat list
                Card(
                    elevation = CardDefaults.elevatedCardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp)
                        .animateContentSize(animationSpec = tween(durationMillis = 500)), // Smooth transition
                    shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp, bottomEnd = 0.dp, bottomStart = 0.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Image for the Card
                        Image(
                            painter = painterResource(id = R.drawable.blck_blurry),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            alpha = 1f // Adjust alpha for background blur effect
                        )

                        // Chat Section inside the Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top =18.dp , start=10.dp, end=10.dp ,bottom=0.dp)
                        ) {
                            Text(
                                text = "Chat",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                fontSize = 20.sp
                            )

                            val chatList = listOf(
                                ChatFeature("Alice", "Hey, how are you?", "10:30 AM", "2"),
                                ChatFeature("Bob", "Let's meet at 5 PM.", "11:15 AM", "3"),
                                ChatFeature("Charlie", "Can you send me the file?", "12:45 PM", "1"),
                                ChatFeature("David", "See you tomorrow!", "1:20 PM", "5"),
                                ChatFeature("Emma", "Lunch at 2?", "1:50 PM", "2"),
                                ChatFeature("Frank", "Did you finish the report?", "2:10 PM", "4"),
                                ChatFeature("Grace", "Call me when you're free.", "2:40 PM", "1"),
                                ChatFeature("Hannah", "Movie night today?", "3:15 PM", "3"),
                                ChatFeature("Ian", "Meeting rescheduled to 6 PM.", "3:45 PM", "2"),
                                ChatFeature("Jack", "Happy Birthday!", "4:00 PM", "6"),
                                ChatFeature("Kelly", "See you in 10 minutes.", "4:30 PM", "1"),
                                ChatFeature("Leo", "Can we reschedule?", "5:00 PM", "2"),
                                ChatFeature("Mia", "Where are you?", "5:45 PM", "4"),
                                ChatFeature("Nathan", "Good night!", "10:10 PM", "1")
                            )
                            // LazyColumn for the chat list
                            LazyColumn(state = lazyScrollState) {


                                items(chatList, key = { it.name }){
                                     ChatBox(it)
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun ChatBox(chatFeature: ChatFeature) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.person_placeholder_4),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${chatFeature.name}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${chatFeature.message}",
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${chatFeature.time}",
                color = Color.White,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(SkyBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${chatFeature.totalMessage}", color = Color.White, fontSize = 8.sp)
            }
        }

    }
}

@Immutable
@Stable
data class ChatFeature(
    val name:String,
    val message :String,
    val time:String,
    val totalMessage:String
)
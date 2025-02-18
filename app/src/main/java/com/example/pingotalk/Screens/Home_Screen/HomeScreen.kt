package com.example.pingotalk.Screens.Home_Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.Model.User
import com.example.pingotalk.R
import com.example.pingotalk.Screens.Home_Screen.viewmodel.HomeViewModel
import com.example.pingotalk.Utils.ChatBox
import com.example.pingotalk.Utils.CustomDialog
import com.example.pingotalk.ui.theme.FloatButton
import com.example.pingotalk.ui.theme.SkyBlue

@Composable
fun HomeScreen(
    MoveToChatScreen: (chatId: ChatData) -> Unit,
    GoToProfile: () -> Unit,
    GoToSearch: () -> Unit,
    user: User
) {
    val homeViewModel :HomeViewModel = hiltViewModel()
//    LaunchedEffect(Unit){
//        homeViewModel.observeChatPartners()
//    }
    val chatList = homeViewModel._chat.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        CustomDialog({ showDialog.value = false }, { email -> homeViewModel.addPartner(email) })
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier.padding(end = 20.dp, bottom = 25.dp), containerColor = FloatButton
        ) {
            Icon(
                imageVector = Icons.Filled.InsertComment,
                contentDescription = null,
                tint = Color.White
            )
        }
    }) {
        padding ->
        val scrollState = rememberScrollState()
        val lazyScrollState = rememberLazyListState()

        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.blck_blurry),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )


            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .let {
                            if (lazyScrollState.firstVisibleItemIndex > 0) {
                                it.height(0.dp)
                            } else {
                                it
                            }
                        }
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column() {
                            Text(text = "Hello", color = Color.White, fontSize = 15.sp)
                            Text(
                                text = "${user.name}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(.4f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(FloatButton), // Dark Blue
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            GoToSearch()
                                        }, tint = Color.White
                                )
                            }
                            AsyncImage(
                                model = user.photoUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable {
                                        GoToProfile()
                                    }
                            )
                        }
                    }
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

                Card(
                    elevation = CardDefaults.elevatedCardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp)
                        .animateContentSize(animationSpec = tween(durationMillis = 500)), // Smooth transition
                    shape = RoundedCornerShape(
                        topEnd = 15.dp,
                        topStart = 15.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    ),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.blck_blurry),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            alpha = 1f // Adjust alpha for background blur effect
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
                        ) {
                            Text(
                                text = "Chat",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                            LazyColumn(state = lazyScrollState) {
                                items(chatList.value) {
                                    ChatBox(it,MoveToChatScreen)
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}




package com.example.pingotalk.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pingotalk.Model.ChatData
import com.example.pingotalk.ui.theme.SkyBlue

@Composable
fun ChatBox(chatFeature: ChatData, MoveToChatScreen: (chatId: ChatData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Transparent)
            .clickable {
                MoveToChatScreen(chatFeature)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        ) {
            AsyncImage(
                model = "${chatFeature.user2.ppurl}",
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${chatFeature.user2.username}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (chatFeature.last?.content != "") {
                Text(
                    text = "${chatFeature.last?.content}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            } else {
                Text(
                    text = "${chatFeature.user2.bio}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (chatFeature.last?.time == null) {
                Text(
                    text = "",
                    color = Color.White,
                    fontSize = 12.sp
                )
            } else {
                Text(
                    text = "${chatFeature.last.time}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            if (chatFeature.user2.unread > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(SkyBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${chatFeature.user2.unread}", color = Color.White, fontSize = 8.sp)
                }
            } else {
                Text(text = "", color = Color.White, fontSize = 8.sp)
            }
        }
    }
}


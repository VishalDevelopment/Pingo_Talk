package com.example.pingotalk.Ui_Layer.Utils


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pingotalk.Ui_Layer.Screens.Chat_Screen.ChatScreen
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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





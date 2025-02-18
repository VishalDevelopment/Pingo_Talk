package com.example.pingotalk.Screens.Profile_Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pingotalk.Model.User
import com.example.pingotalk.R
import com.example.pingotalk.Screens.Profile_Screen.viewmodel.ProfileViewmodel
import com.example.pingotalk.ui.theme.FloatButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(BackToHome: () -> Boolean, ClearEachStack: () -> Unit, user: MutableState<User?>) {
    val viewmodel:ProfileViewmodel = hiltViewModel()
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF5089C6), Color(0xFF003366))
                        )
                    )
            ) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {
                        IconButton(onClick = { BackToHome() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Personal Info",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(model =  user.value?.photoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Name and Email
                Text(
                    text = "${user.value?.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${user.value?.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))

                val context = LocalContext.current
                // Buttons
                Button(
                    onClick = {
                        Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f), colors = ButtonDefaults.buttonColors(containerColor = FloatButton)
                ) {
                    Text(text = "Edit Profile")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        viewmodel.SignoutApp()
                        ClearEachStack()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = "Log Out")
                }
            }
        }
    )
}

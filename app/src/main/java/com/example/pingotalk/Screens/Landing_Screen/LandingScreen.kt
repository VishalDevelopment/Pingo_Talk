package com.example.pingotalk.Screens.Landing_Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pingotalk.R
import com.example.pingotalk.Screens.Landing_Screen.viewmodel.GoogleSignInViewModel


@Composable
fun SignInScreen(navController: NavHostController) {
    val viewmodel: GoogleSignInViewModel = hiltViewModel()
    val context = LocalContext.current
    val buttonBrush = Brush.linearGradient(
        listOf(
            Color(0xFF238CDD),
            Color(0xFF255DCC)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_blur),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.boyonchair),
                contentDescription = null
            )
            Text(
                text = "Pingo Talk !!",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Text(
                text = "Stay close, no matter the distance—real-time messaging that keeps you connected anytime, anywhere.",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400, overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(vertical = 25.dp))
            OutlinedButton(
                onClick = {

                    // Handle Google One Tap Sign In
                   viewmodel.handleGoogleSignIn(context, navController)

                },
                modifier = Modifier
                    .background(buttonBrush, CircleShape)
                    .fillMaxWidth(.7f)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Continue with Google")

                    Image(
                        painter = painterResource(id = R.drawable.googleicon),
                        contentDescription = null,
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }
    }
}
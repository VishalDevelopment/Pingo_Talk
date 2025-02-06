package com.example.pingotalk.Utils

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable

fun CustomDialog(
    RemoveDialog: () -> Unit,
    AddEmailToFireStore: (email: String) -> Unit
) {
    BackHandler {
        RemoveDialog()
    }
    val email = remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = {
        RemoveDialog()
    }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Enter Email Id",
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center
                    )
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                        }, label = {
                            Text(text = "Email")
                        }, shape = RoundedCornerShape(20.dp)
                    )

                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        OutlinedButton(onClick = {
                            RemoveDialog()
                        }) {
                            Text(text = "Cancel", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp))
                        OutlinedButton(onClick = {
                            if (email.value!=""){
                                AddEmailToFireStore(email.value)
                                RemoveDialog()
                            }else{

                            }

                        }) {
                            Text(text = "Add", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
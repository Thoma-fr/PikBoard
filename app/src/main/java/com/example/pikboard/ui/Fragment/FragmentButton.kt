package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PikButton(text: String = "",isLoading: Boolean = false, action: () -> Unit) {
    Button(
        onClick = { if (!isLoading) action() },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp)
    ){
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(20.dp)
            )
        } else {
            Text(text)
        }
    }
}

@Preview
@Composable
fun PikButtonPreview(){
    PikButton(
        text="Text",
        false,
        action = {}
    )
}
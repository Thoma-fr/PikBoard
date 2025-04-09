package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
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
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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

@Composable
fun PikBigButton(text: String = "", isLoading: Boolean= false, action: () -> Unit) {
    Button(
        onClick = {if (!isLoading) action()},
        modifier = Modifier.size(150.dp),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(20.dp)
                )
            } else {
                Text(text = text, textAlign = TextAlign.Center)
            }
        }
    }
}
@Preview
@Composable
fun PikBigButtonPreview() {
    PikBigButton(text="This is a big button") {  }
}
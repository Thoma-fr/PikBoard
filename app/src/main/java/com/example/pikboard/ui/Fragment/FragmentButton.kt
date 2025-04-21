package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp

@Composable
fun PikButton(text: String = "",isLoading: Boolean = false, color:Color = Color.Unspecified, action: () -> Unit) {
    Button(
        onClick = { if (!isLoading) action() },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
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
fun PikMedButton(
    text: String = "",
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Button(
        onClick = { if (!isLoading) action() },
        modifier = modifier
            .height(60.dp)
            .defaultMinSize(minWidth = 140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        enabled = !isLoading
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            }
        }
    }
}
@Preview
@Composable
fun PikMedButton() {
    PikMedButton(text="This is a med button") {  }
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
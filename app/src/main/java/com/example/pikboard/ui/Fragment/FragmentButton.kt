package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PikButton(text: String = "", action: () -> Unit) {
    Button(
        onClick = action,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp)
    ){
        Text(text = text)
    }
}

@Preview
@Composable
fun PikButtonPreview(){
    PikButton(
        text="Text",
        {}
    )
}
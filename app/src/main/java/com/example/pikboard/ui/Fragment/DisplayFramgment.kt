package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun PikHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "PikBoard",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PikHeaderPreview() {
    PikHeader()
}
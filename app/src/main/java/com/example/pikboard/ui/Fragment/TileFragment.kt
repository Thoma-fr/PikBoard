package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pikboard.R

@Composable
fun Tile(name: String)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Adversaire ".plus(name))
            Image(
                painter = painterResource(id = R.drawable.phchess),
                contentDescription = "",
                modifier = Modifier.height(150.dp)
            )
            Text(text = "Votre tour")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DisplayTilesPreview() {
    Tile(name = "toto")
}
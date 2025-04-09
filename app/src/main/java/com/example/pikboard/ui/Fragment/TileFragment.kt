package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pikboard.R

@Composable
fun Tile(name: String="Name",
         fem: String,
         onClick: () -> Unit)
{
    Surface(
        modifier = Modifier.padding(horizontal = 8.dp),
        tonalElevation = 3.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onClick() }
        ) {

            Text(
                text = "Adversaire $name",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            val url = "https://parrot-lucky-partially.ngrok-free.app/v1/chess?q=$fem"
            TileImage(url, 150.dp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DisplayTilesPreview() {
    Tile(name = "toto", fem = "r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1", {})
}
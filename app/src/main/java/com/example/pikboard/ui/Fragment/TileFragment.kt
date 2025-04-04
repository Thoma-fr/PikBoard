package com.example.pikboard.ui.Fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
fun Tile(name: String="Name",
         fem: String,
         onClick: () -> Unit)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    )
    {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Adversaire ".plus(name))

            val url ="https://gnat-happy-drake.ngrok-free.app/v1/chess?q="+fem
            TileImage(url,150.dp)

        }
    }
}
@Preview(showBackground = true)
@Composable
fun DisplayTilesPreview() {
//    Tile(name = "toto", fem = " ", {})
}
package com.example.pikboard.ui.Fragment

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pikboard.R

@Composable
fun FriendTile(name: String = "BIBI") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FriendTilePreview() {
    FriendTile()
}
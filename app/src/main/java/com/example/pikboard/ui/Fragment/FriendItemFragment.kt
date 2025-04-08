package com.example.pikboard.ui.Fragment

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pikboard.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
@Composable
fun FriendTile(name: String = "BIBI") {
    Surface( shape = MaterialTheme.shapes.large) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
    }
}

@Composable
@Preview(showBackground = true)
fun FriendTilePreview() {
    FriendTile()
}
package com.example.pikboard.ui.Fragment

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileImage(url:String, size: Dp) {
     val painter = rememberAsyncImagePainter(
         model = ImageRequest.Builder(LocalContext.current)
             .data(url)
             .crossfade(true)
             .build()
     )

     Box(modifier = Modifier.size(size)
         .clip(RoundedCornerShape(8.dp))
     ) {
         if (painter.state.value is AsyncImagePainter.State.Loading) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Gray),
                 contentAlignment = Alignment.Center
             ) {
                 CircularProgressIndicator()
             }
         }

         Image(
             painter = painter,
             contentDescription = "profile image",
             modifier = Modifier.fillMaxSize()

         )
     }
}

@Preview(showBackground = true)
@Composable
fun ProfileImagePreview() {
    ProfileImage("", 180.dp)
}

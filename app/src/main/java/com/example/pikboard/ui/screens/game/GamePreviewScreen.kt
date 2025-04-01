package com.example.pikboard.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.ui.Fragment.PikHeader

@Composable
fun GamePreviewScreen(navController: NavController, sharedViewModel: SharedImageViewModel) {
    val imageUri = sharedViewModel.selectedImageBitmap

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PikHeader(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text="New Game")

        if (imageUri != null) {
                Image(
                    bitmap = imageUri.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            } else {
            Text(text = "Aucune image sélectionnée")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreviewScreenPreview() {
    GamePreviewScreen(rememberNavController(), viewModel())
}
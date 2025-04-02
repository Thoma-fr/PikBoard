package com.example.pikboard.ui.screens.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.screens.Routes
import kotlin.contracts.contract

@Composable
fun AddGamePage( navController: NavController, sharedViewModel: SharedImageViewModel) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(contract= ActivityResultContracts.GetContent()) {
        uri ->

        val inputStream = context.contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        sharedViewModel.setImageBitmap(bitmap)
        navController.navigate(Routes.Game.PREVIEW)

    }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            sharedViewModel.setImageBitmap(it)
            navController.navigate(Routes.Game.PREVIEW)
        }
    }

    val camPermission = rememberLauncherForActivityResult(contract=ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if(isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val photoPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if(isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PikHeader(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text="New Game")

        PikButton(text="From Scratch") {
            sharedViewModel.setCurrentFenP("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1")
            navController.navigate(Routes.Game.PREVIEW)
        }
        PikButton(text="From camera") {
            camPermission.launch(android.Manifest.permission.CAMERA)
        }
        PikButton(text="From gallery") {
            photoPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddGamePagePreview() {
    AddGamePage(rememberNavController(), viewModel())
}
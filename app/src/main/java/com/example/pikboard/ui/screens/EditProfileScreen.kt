package com.example.pikboard.ui.screens

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.extensions.toMultipartBodyPart
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikBigButton
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.Fragment.PikPasswordField
import com.example.pikboard.ui.Fragment.PikPasswordFieldPreview
import com.example.pikboard.ui.Fragment.PikTextField

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isNewImageLoading by remember { mutableStateOf(false) }
    var isUpdatePasswordLoading by remember { mutableStateOf(false) }
    var errorApiUpdateImageMessage by remember { mutableStateOf("") }
    var errorApiUpdatePasswordMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    val galleryLauncher = rememberLauncherForActivityResult(contract= ActivityResultContracts.GetContent()) {
            uri ->
        if(uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val imagePart = bitmap!!.toMultipartBodyPart("profile_image")

            pikBoardApiViewModel.updateProfileImage(token as String, imagePart)
        }
    }

    val gameResult = pikBoardApiViewModel.imageProfileResponse.observeAsState()
    val updatePasswordResult = pikBoardApiViewModel.updatePasswordResponse.observeAsState()

    when(val result = gameResult.value) {
        is NetworkResponse.Error -> {
            errorApiUpdateImageMessage = result.message
            isNewImageLoading = false
        }
        NetworkResponse.Loading -> {
            isNewImageLoading = true
        }
        is NetworkResponse.Success -> {
            navController.navigate(Routes.PROFILE_PAGE)
            pikBoardApiViewModel.resetUpdateProfileImage()
        }
        null -> {}
    }

    when(val result = updatePasswordResult.value) {
        is NetworkResponse.Error -> {
            errorApiUpdatePasswordMessage = result.message
            isUpdatePasswordLoading = false
        }
        NetworkResponse.Loading -> {
            isUpdatePasswordLoading = true
        }
        is NetworkResponse.Success -> {
            isUpdatePasswordLoading = false
            pikBoardApiViewModel.resetUpdatePassword()
            navController.navigate(Routes.PROFILE_PAGE)
        }
        null -> {}
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
        PikHeader()

        Spacer(modifier = Modifier.height(16.dp))

        PikBigButton("Update profile image", isNewImageLoading) {
            isNewImageLoading = true
            photoPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (errorApiUpdateImageMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorApiUpdateImageMessage, fontSize = 16.sp, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Update Password")

        Spacer(modifier = Modifier.height(16.dp))

        PikPasswordField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            hint = "Old Password...",
        )

        Spacer(modifier = Modifier.height(16.dp))

        PikPasswordField(
            value = newPassword,
            onValueChange = { newPassword = it },
            hint = "New Password...",
        )

        if (errorApiUpdatePasswordMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorApiUpdateImageMessage, fontSize = 16.sp, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        PikButton(text="Update password", isUpdatePasswordLoading) {
            if (oldPassword.isNotBlank() && newPassword.isNotBlank()){
                pikBoardApiViewModel.updatePassword(token as String,oldPassword,newPassword)
            } else {
                errorApiUpdatePasswordMessage = "Old and New password are mandatory"
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(rememberNavController(), viewModel(), viewModel())
}
package com.example.pikboard.ui.screens.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.extensions.toMultipartBodyPart
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.screens.Routes

@Composable
fun GamePreviewScreen(
    navController: NavController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel,
) {
    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    val imageUri = sharedViewModel.selectedImageBitmap

    var apiCallMade by remember { mutableStateOf(false) }
    var errorApiMessage by remember { mutableStateOf("") }
    var isLoading by remember{ mutableStateOf(false) }
    var fen by remember { mutableStateOf("") }

    val imageToFenResponse = pikBoardApiViewModel.imageToFenResponse.observeAsState()
    LaunchedEffect(token) {
        if (token is String && (token as String).isNotEmpty() && apiCallMade == false ) {
            if (imageUri != null) {
                val imagePart = imageUri!!.toMultipartBodyPart("img")
                pikBoardApiViewModel.imageToFen(token as String, imagePart)
                apiCallMade = true
            } else {
                fen = sharedViewModel.pcurrentFen
            }
        }
    }

    when(val result = imageToFenResponse.value) {
        is NetworkResponse.Error -> {
            errorApiMessage = result.message
            isLoading = false
        }
        NetworkResponse.Loading -> {
            isLoading = true
        }
        is NetworkResponse.Success -> {
            fen = result.data.data
            isLoading = false
        }
        null -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PikHeader(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text="Config position",fontSize = 50.sp,fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
        if (!isLoading) {

            Text(text = "fen > $fen")

            // TODO: Mettre ici la modification du board
            Text("TODO: Chess board goes here")
            Spacer(modifier = Modifier.height(30.dp))
            PikButton("Validate"){
                // INFO: This is the final fen sent to the game page
                sharedViewModel.setCurrentFenP(fen)
                navController.navigate(Routes.Game.FRIEND)
            }

        } else {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
}
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreviewScreenPreview() {
    GamePreviewScreen(rememberNavController(), viewModel(), PikBoardApiViewModel())
}
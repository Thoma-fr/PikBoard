package com.example.pikboard.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.UserApi
import com.example.pikboard.api.UserResponse
import com.example.pikboard.store.readSessionToken
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(pikBoardApiViewModel: PikBoardApiViewModel) {
    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    var user by remember { mutableStateOf<UserApi?>(null) }
    var apiCallMade by remember { mutableStateOf(false) }
    var errorApiMessage by remember { mutableStateOf("") }

    LaunchedEffect(token) {
        if (token is String && (token as String).isNotEmpty() && apiCallMade == false) {
            pikBoardApiViewModel.getUserFromSessionToken(token as String)
            apiCallMade = true
        }
    }

    val loginResult = pikBoardApiViewModel.userFromSessionTokenResponse.observeAsState()

    when(val result = loginResult.value) {
        is NetworkResponse.Error -> {
            errorApiMessage = result.message
        }
        NetworkResponse.Loading -> {
        }
        is NetworkResponse.Success -> {
            user = result.data.data
        }
        null -> {}
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is the ProfilePage page $token")
        if (user != null) {
            Text(text = "Username: ${user!!.username}")
            Text(text = "Email: ${user!!.email}")
        } else if (errorApiMessage.isNotEmpty()) {
            Text(text= "Api error message")
        } else {
            Text(text = "Chargement des informations utilisateur...")
        }
    }
}

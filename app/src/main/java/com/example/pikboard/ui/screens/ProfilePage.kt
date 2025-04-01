package com.example.pikboard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.UserApi
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.FriendScore
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.ProfileImage

@Composable
fun ProfilePage(pikBoardApiViewModel: PikBoardApiViewModel) {
    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    var user by remember { mutableStateOf<UserApi?>(null) }
    var apiCallMade by remember { mutableStateOf(false) }
    var errorApiMessage by remember { mutableStateOf("") }
    var isLoading by remember{ mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
            isLoading = false
        }
        NetworkResponse.Loading -> {
            isLoading = true
        }
        is NetworkResponse.Success -> {
            user = result.data.data
            isLoading = false
        }
        null -> {}
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(20.dp)
            )
        } else if (errorApiMessage.isNotEmpty()){
            Text(text= "Ther is an error with the api : $errorApiMessage")
        }else {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                ) {
                    ProfileImage(
                        url = "https://gnat-happy-drake.ngrok-free.app/v1/chess?q=r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",
                        150.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (user != null) {
                        Column {
                            Text(text = "${user!!.username}")
                            Text(text = "${user!!.email}")
                            Text(text = "${user!!.phone} <- ici aussi todo")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text="Registered since ... bahhh todo:")
            }

            Spacer(modifier = Modifier.height(8.dp))

            PikButton(text = "Edit", false) {  }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "History")

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                repeat(7) { _ ->
                    FriendScore("user one", "user two", "1", "0"){}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview(){
    ProfilePage(PikBoardApiViewModel())
}
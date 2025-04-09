package com.example.pikboard.ui.screens.game

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.UserApi
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.Fragment.PikTextField
import com.example.pikboard.ui.screens.FriendRequestTile
import com.example.pikboard.ui.screens.Routes
import com.example.pikboard.ui.screens.SearchResultTile

@Composable
fun GameFriendSreen(
    navController: NavHostController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel
) {

    val friendsResponse by pikBoardApiViewModel.friendsResponse.observeAsState()
    val createGameResponse by pikBoardApiViewModel.createGameResponse.observeAsState()

    var friends by remember { mutableStateOf<List<UserApi>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorApiMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val tokenValue by readSessionToken(context).collectAsState(initial = "")
    val token = if (tokenValue is String) tokenValue as String else ""



    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            pikBoardApiViewModel.getFriends(token)
        }
    }

    LaunchedEffect(friendsResponse) {
        when(val result = friendsResponse) {
            is NetworkResponse.Success -> {
                friends = result.data.data
                isLoading = false
            }
            is NetworkResponse.Error -> {
                errorApiMessage = result.message
                isLoading = false
            }
            NetworkResponse.Loading -> {
                isLoading = true
            }
            null -> {}
        }
    }
    
    when(val result = createGameResponse) {
        is NetworkResponse.Error -> {
            errorApiMessage = result.message
            isLoading = false
        }
        NetworkResponse.Loading -> {
            isLoading = true
        }
        is NetworkResponse.Success<*> -> {
            isLoading = false
            navController.navigate(Routes.Game.CHESS)
            pikBoardApiViewModel.resetCreateGameResponse()
        }
        null -> {}
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        PikHeader()

        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (!errorApiMessage.isNullOrBlank()) {
            Text(text=errorApiMessage)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Invite a friend to play",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(friends) { user ->
                    SearchResultTile(
                        name = user.username,
                        img = user.image,
                        {
                            sharedViewModel.setCurrentOpponent(user)
                            pikBoardApiViewModel.createGame(
                                token ,
                                sharedViewModel.pcurrentFen,
                                user.id
                            )
    //                        navController.navigate(Routes.Game.CHESS)
                        }
                    )
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GameFriendScreenPreview() {
    GameFriendSreen(rememberNavController(), viewModel(), viewModel())
}
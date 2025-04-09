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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.CurrentGame
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.UserApi
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.FriendScore
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.ProfileImage
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProfilePage(
    navController: NavHostController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel
) {
    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    var user by remember { mutableStateOf<UserApi?>(null) }
    var games by remember { mutableStateOf<List<CurrentGame>>(emptyList()) }
    var apiCallMade by remember { mutableStateOf(false) }
    var errorApiMessage by remember { mutableStateOf("") }
    var isLoading by remember{ mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(token) {
        if (token is String && (token as String).isNotEmpty() && apiCallMade == false) {
            pikBoardApiViewModel.getUserFromSessionToken(token as String)
            pikBoardApiViewModel.endedGames(token as String)
            apiCallMade = true
        }
    }

    val loginResult = pikBoardApiViewModel.userFromSessionTokenResponse.observeAsState()
    val gameHistoryResult = pikBoardApiViewModel.endedGamesResponse.observeAsState()

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

    when(val result = gameHistoryResult.value) {
        is NetworkResponse.Error -> {
            errorApiMessage = result.message
        }
        NetworkResponse.Loading -> {
            isLoading = true
        }
        is NetworkResponse.Success -> {
            games = result.data.data
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
            Text(text= "There is an error with the api : $errorApiMessage")
        }else {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (user != null) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        Row(
                        ) {
                            ProfileImage(
                                url = user!!.image,

                                150.dp
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(text = "${user!!.username}")
                                Text(text = "${user!!.email}")

                                Spacer(modifier = Modifier.height(20.dp))

                                user?.created_at?.let { createdAtString ->
                                    val parsedDate = ZonedDateTime.parse(createdAtString)
                                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                                    val formattedDate = parsedDate.format(formatter)
                                    Text(text = "Registered since: $formattedDate", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            PikButton(text = "Edit", false) {
                navController.navigate(Routes.EDIT_PROFILE)
            }

            PikButton(text = "Disconnect", false, Color.Red) {
                pikBoardApiViewModel.resetLogin()
                navController.navigate(Routes.Auth.LOGIN_PAGE)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "History")

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                games.forEach { game ->
                    FriendScore(
                        game.user.username,
                        game.opponent.username,
                        "",
                        ""
                    ) {
                        sharedViewModel.setCurrentFenP(game.board)
                        navController.navigate(Routes.Game.CHESS)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview(){
    ProfilePage(rememberNavController(), viewModel(), PikBoardApiViewModel())
}
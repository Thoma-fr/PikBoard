package com.example.pikboard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.CurrentGame
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.UserApi
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.Fragment.Tile

@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val token by readSessionToken(context).collectAsState(initial = "")

        var games by remember { mutableStateOf<List<CurrentGame>>(emptyList<CurrentGame>()) }
        var apiCallMade by remember { mutableStateOf(false) }
        var errorApiMessage by remember { mutableStateOf("") }
        var isLoading by remember{ mutableStateOf(false) }
        var user by remember{ mutableStateOf<UserApi?>(null) }

        LaunchedEffect (token) {
            if (token is String && (token as String).isNotEmpty() && apiCallMade == false) {
                pikBoardApiViewModel.currentGame(token as String)
                pikBoardApiViewModel.getUserFromSessionToken(token as String)
                apiCallMade = true
            }
        }

        val gameResult = pikBoardApiViewModel.currentGamesResponse.observeAsState()

        when(val result = gameResult.value) {
            is NetworkResponse.Error -> {
                errorApiMessage = result.message
                isLoading = false
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

        val loginResult = pikBoardApiViewModel.userFromSessionTokenResponse.observeAsState()

        when(val result = loginResult.value) {
            is NetworkResponse.Error -> {
                errorApiMessage = result.message
            }
            NetworkResponse.Loading -> {
            }
            is NetworkResponse.Success -> {
                user = result.data.data
                sharedViewModel.updateUserID(result.data.data.id)
            }
            null -> {}
        }

        PikHeader(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(16.dp))

        PikButton("New Game") {
            navController.navigate(Routes.Game.NEW)
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Continue game")

        Spacer(modifier = Modifier.height(24.dp))

        if (games != null && games.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(games) { game ->
                    var opponent = ""
                    if (user?.username == game.opponent.username) {
                        opponent = game.user.username
                    } else {
                        opponent = game.opponent.username
                    }
                    Tile(name = opponent, fem = game.board, onClick = {
                        sharedViewModel.setCurrentFenP(game.board)
                        sharedViewModel.setCurrentGameID(game.id)
                        sharedViewModel.updateGameWhitePlayer(game.white_player_id)
                        navController.navigate(Routes.Game.CHESS)
                    })
                }
            }
        } else {
            Text(text = "You do not have games to continue")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel())
}
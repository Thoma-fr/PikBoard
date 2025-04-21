package com.example.pikboard.ui.screens.game

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.R
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.databaseFirebase.ChessGameViewModel
import com.example.pikboard.chess.ChessBoard
import com.example.pikboard.chess.ChessGameState
import com.example.pikboard.chess.ChessRules
import com.example.pikboard.chess.parseFEN
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader

@Composable
fun GameScreen(
    navController: NavHostController,
    sharedViewModel: SharedImageViewModel,
    pikBoardApiViewModel: PikBoardApiViewModel,
    firebaseViewModel: ChessGameViewModel
) {
    PikHeader()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    val game = firebaseViewModel.game.value
    var fenPosition = game?.board ?: sharedViewModel.pcurrentFen
    var gameID = sharedViewModel.currentGameID

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                val id = game?.id
                if (id != null) {
                    pikBoardApiViewModel.updateGame(token as String, id, fenPosition)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val playerIsWhite = sharedViewModel.currentGameWhitePlayer == sharedViewModel.userID
            val isWhiteTurn = fenPosition.split(" ")[1] == "w"

            val pieces = remember(fenPosition) { parseFEN(fenPosition) }
            val gameState = remember(fenPosition) { ChessGameState.fromFEN(fenPosition) }
            val isInCheck = remember(fenPosition) { ChessRules.isInCheck(pieces, isWhiteTurn) }
            val isCheckmate =
                remember(fenPosition) { ChessRules.isCheckmate(pieces, isWhiteTurn, gameState) }

            LaunchedEffect(Unit) {
                if (gameID != null) {
                    firebaseViewModel.observeGameById(gameID)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = "Tour: ${if (isWhiteTurn) "Blancs" else "Noirs"} | Vous: ${if (playerIsWhite) "Blancs" else "Noirs"} ${game?.id}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "${sharedViewModel.currentGameWhitePlayer} ${sharedViewModel.userID}"
                )


                if (isInCheck || isCheckmate) {
                    Text(
                        text = if (isCheckmate) {
                            "ÉCHEC ET MAT ! ${if (isWhiteTurn) "Noirs" else "Blancs"} gagnent."
                        } else {
                            "ÉCHEC au roi ${if (isWhiteTurn) "blanc" else "noir"} !"
                        },
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (isCheckmate) {
                    pikBoardApiViewModel.endGames(token as String, sharedViewModel.userID!!, gameID!!)
                }

                ChessBoard(
                    fen = fenPosition,
                    playerIsWhite = playerIsWhite,
                    onMove = { newFen ->
                        sharedViewModel.setCurrentFenP(newFen)
                        fenPosition = newFen
                        val toPlay = if (newFen.split(" ")[1] == "w") "Blancs" else "Noirs"
                        if (gameID != null) {
                            firebaseViewModel.updateBoardById(gameID, fenPosition)
                        }
                        Toast.makeText(context, "Au tour des $toPlay", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(16.dp)
                )
                Column {
                    PikButton(text="Draw") {  }
                    PikButton(text="Surrender") {  }
                }
            }
        }


    }
}
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val fakeViewModel = SharedImageViewModel().apply {
        pcurrentFen = "8/8/8/8/8/8/8/8 w - - 0 1"
    }
    GameScreen(rememberNavController(), fakeViewModel, viewModel(), viewModel())
}


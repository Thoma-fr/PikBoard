package com.example.pikboard.ui.screens.game

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.R
import com.example.pikboard.chess.ChessBoard
import com.example.pikboard.chess.ChessGameState
import com.example.pikboard.chess.ChessRules
import com.example.pikboard.chess.parseFEN
import com.example.pikboard.store.SharedImageViewModel

@Composable
fun GameScreen(navController: NavHostController, sharedViewModel: SharedImageViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var fenPosition = sharedViewModel.pcurrentFen

            val playerIsWhite = true
            val context = LocalContext.current
            val isWhiteTurn = fenPosition.split(" ")[1] == "w"

            val pieces = remember(fenPosition) { parseFEN(fenPosition) }
            val gameState = remember(fenPosition) { ChessGameState.fromFEN(fenPosition) }
            val isInCheck = remember(fenPosition) { ChessRules.isInCheck(pieces, isWhiteTurn) }
            val isCheckmate =
                remember(fenPosition) { ChessRules.isCheckmate(pieces, isWhiteTurn, gameState) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tour: ${if (isWhiteTurn) "Blancs" else "Noirs"} | Vous: ${if (playerIsWhite) "Blancs" else "Noirs"}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
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

                ChessBoard(
                    fen = fenPosition,
                    playerIsWhite = playerIsWhite,
                    onMove = { newFen ->
//                    sharedViewModel.setCurrentFenP(newFen)
                        fenPosition = newFen
                        val toPlay = if (newFen.split(" ")[1] == "w") "Blancs" else "Noirs"
                        Toast.makeText(context, "Au tour des $toPlay", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.surrender),
            contentDescription = "Logo Giveup",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .offset(y = (-200).dp).size(60.dp)
                .clickable {

                    // add the end game thing
                }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val fakeViewModel = SharedImageViewModel().apply {
        pcurrentFen = "8/8/8/8/8/8/8/8 w - - 0 1"
    }
    GameScreen(rememberNavController(), fakeViewModel)
}


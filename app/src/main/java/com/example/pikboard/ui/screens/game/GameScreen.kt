package com.example.pikboard.ui.screens.game

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pikboard.chess.ChessBoard
import com.example.pikboard.chess.ChessGameState
import com.example.pikboard.chess.ChessRules
import com.example.pikboard.chess.parseFEN

@Composable
fun GameScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var fenPosition by remember {
            mutableStateOf("8/7P/8/2K5/4k3/8/8/8 w - - 0 1")
        }

        val playerIsWhite = true
        val context = LocalContext.current
        val isWhiteTurn = fenPosition.split(" ")[1] == "w"

        val pieces = remember(fenPosition) { parseFEN(fenPosition) }
        val gameState = remember(fenPosition) { ChessGameState.fromFEN(fenPosition) }
        val isInCheck = remember(fenPosition) { ChessRules.isInCheck(pieces, isWhiteTurn) }
        val isCheckmate = remember(fenPosition) { ChessRules.isCheckmate(pieces, isWhiteTurn, gameState) }

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
                    fenPosition = newFen
                    val toPlay = if (newFen.split(" ")[1] == "w") "Blancs" else "Noirs"
                    Toast.makeText(context, "Au tour des $toPlay", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

package com.example.pikboard.ui.screens.game

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.chess.ChessBoard
import com.example.pikboard.chess.ChessPieceDrawer
import com.example.pikboard.chess.ChessGameState
import com.example.pikboard.chess.ChessPiece
import com.example.pikboard.chess.generateFEN
import com.example.pikboard.chess.parseFEN
import com.example.pikboard.extensions.toMultipartBodyPart
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.store.readSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.screens.Routes
import androidx.compose.ui.unit.IntSize
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import android.widget.Toast
import androidx.compose.foundation.layout.heightIn
import com.example.pikboard.chess.ChessRules
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape

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

    val pieces by remember(fen) { mutableStateOf(if(fen.isNotEmpty()) parseFEN(fen) else emptyList()) }
    val gameState by remember(fen) { mutableStateOf(if(fen.isNotEmpty()) ChessGameState.fromFEN(fen) else ChessGameState()) }
    val isWhiteTurn by remember(fen) { mutableStateOf(if (fen.isNotEmpty()) fen.split(" ")[1] == "w" else true) }

    val imageToFenResponse = pikBoardApiViewModel.imageToFenResponse.observeAsState()
    var boardSize by remember { mutableStateOf(IntSize.Zero) }
    var boardOffset by remember { mutableStateOf(Offset.Zero) }
    var pieceToAdd by remember { mutableStateOf<Pair<Char, Boolean>?>(null) }

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

        Text(text="Config position")

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .pointerInput(pieceToAdd) {
                        detectTapGestures(
                            onTap = { offsetTap ->
                                pieceToAdd?.let { (type, isWhite) ->
                                    if (boardSize != IntSize.Zero) {
                                        val localX = offsetTap.x
                                        val localY = offsetTap.y
                                        val squareSize = boardSize.width.toFloat() / 8f

                                        if (localX >= 0 && localX < boardSize.width && localY >= 0 && localY < boardSize.height) {
                                            val file = (localX / squareSize).toInt().coerceIn(0, 7)
                                            val rank = 7 - (localY / squareSize).toInt().coerceIn(0, 7)
                                            val targetPosition = Pair(file, rank)

                                            val newPiece = ChessPiece(type = type, isWhite = isWhite, position = targetPosition)
                                            val updatedPieces = pieces.filterNot { it.position == targetPosition } + newPiece
                                            val newFen = generateFEN(updatedPieces, isWhiteTurn, gameState)
                                            fen = newFen
                                            sharedViewModel.setCurrentFenP(newFen)

                                            pieceToAdd = null
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                ChessBoard(
                    fen = fen,
                    playerIsWhite = true,
                    onMove = { newFen ->
                        fen = newFen
                        sharedViewModel.setCurrentFenP(newFen)
                    },
                    modifier = Modifier
                        .matchParentSize()
                        .onGloballyPositioned { layoutCoordinates ->
                            boardSize = layoutCoordinates.size
                        },
                    freeMove = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(text = "Add piece (click below, then on the board)")
                PieceSourceRow(isWhite = true, pieceToAdd = pieceToAdd) { type ->
                    pieceToAdd = Pair(type, true)
                }
                Spacer(modifier = Modifier.height(4.dp))
                PieceSourceRow(isWhite = false, pieceToAdd = pieceToAdd) { type ->
                    pieceToAdd = Pair(type, false)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            PikButton("Validate"){
                val currentPieces = parseFEN(fen)
                val currentGameState = ChessGameState.fromFEN(fen)

                // Validation 1: Vérifier les rois
                val whiteKings = currentPieces.count { it.type == 'K' && it.isWhite }
                val blackKings = currentPieces.count { it.type == 'K' && !it.isWhite }

                if (whiteKings != 1 || blackKings != 1) {
                    Toast.makeText(context, "Error: There must be exactly one king of each color.", Toast.LENGTH_LONG).show()
                    return@PikButton // Arrêter l'action
                }

                // Validation 2: Vérifier l'échec et mat (pour les deux couleurs)
                val isWhiteMated = ChessRules.isCheckmate(currentPieces, true, currentGameState)
                val isBlackMated = ChessRules.isCheckmate(currentPieces, false, currentGameState)

                if (isWhiteMated || isBlackMated) {
                    Toast.makeText(context, "Error: The position is already in checkmate.", Toast.LENGTH_LONG).show()
                    return@PikButton // Arrêter l'action
                }

                // Si tout est valide, continuer
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

@Preview(showBackground = true)
@Composable
fun GamePreviewScreenPreview() {
    GamePreviewScreen(rememberNavController(), viewModel(), PikBoardApiViewModel())
}

@Composable
fun PieceSourceRow(
    isWhite: Boolean,
    pieceToAdd: Pair<Char, Boolean>?,
    onPieceSelected: (Char) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf('K', 'Q', 'R', 'B', 'N', 'P').forEach { type ->
            val pieceChar = if (isWhite) ChessPieceDrawer.pieceCharsOutline[type] else ChessPieceDrawer.pieceCharsFilled[type]
            val isSelected = pieceToAdd?.first == type && pieceToAdd.second == isWhite
            val backgroundColor = if (isSelected) Color.Yellow.copy(alpha = 0.5f) else Color.Transparent

            Text(
                text = pieceChar ?: "?",
                fontSize = 32.sp,
                modifier = Modifier
                    .background(backgroundColor, shape = CircleShape)
                    .clickable { onPieceSelected(type) }
                    .padding(4.dp)
            )
        }
    }
}
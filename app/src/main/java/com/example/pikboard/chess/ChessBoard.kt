package com.example.pikboard.chess

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pikboard.chess.ChessPieceDrawer

data class ChessPiece(
    val type: Char,
    val isWhite: Boolean,
    val position: Pair<Int, Int>
)

@Composable
fun ChessBoard(
    fen: String,
    playerIsWhite: Boolean = true,
    onMove: ((newFen: String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    freeMove: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    val isWhiteTurn = extractTurnFromFEN(fen)
    val isPlayerTurn = if (freeMove) true else isWhiteTurn == playerIsWhite
    val gameState = remember(fen) { ChessGameState.fromFEN(fen) }
    
    var pieces by remember(fen) { mutableStateOf(parseFEN(fen)) }
    var selectedPiece by remember { mutableStateOf<ChessPiece?>(null) }
    var fingerPosition by remember { mutableStateOf(Offset.Zero) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var startDragPosition by remember { mutableStateOf(Offset.Zero) }
    var possibleMoves by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    
    var showPromotionDialog by remember { mutableStateOf(false) }
    var pendingPromotion by remember { mutableStateOf<Triple<Pair<Int, Int>, Pair<Int, Int>, ChessPiece>?>(null) }
    
    val isInCheck = remember(pieces) { ChessRules.isInCheck(pieces, isWhiteTurn) }
    val isCheckmate = remember(pieces) { ChessRules.isCheckmate(pieces, isWhiteTurn, gameState) }

    if (showPromotionDialog) {
        PromotionDialog(
            isWhite = pendingPromotion?.third?.isWhite ?: true,
            onPieceSelected = { promotionPiece ->
                pendingPromotion?.let { (from, to, piece) ->
                    val updatedPieces = pieces.map { p ->
                        if (p == piece) {
                            p.copy(type = promotionPiece, position = to)
                        } else if (p.position == to) {
                            p.copy(position = Pair(-1, -1))
                        } else p
                    }.filter { it.position.first >= 0 }
                    
                    pieces = updatedPieces
                    
                    val updatedGameState = gameState.afterMove(from, to, piece, updatedPieces)
                    val newFen = generateFEN(updatedPieces, !isWhiteTurn, updatedGameState)
                    onMove?.invoke(newFen)
                }
                showPromotionDialog = false
                pendingPromotion = null
            }
        )
    }

    Box(modifier = modifier) {
        if (isInCheck || isCheckmate) {
            Text(
                text = if (isCheckmate) "Échec et mat!" else "Échec!",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            )
        }
        
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(fen) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (isPlayerTurn && !isCheckmate) {
                                val squareSize = size.width.toFloat() / 8f
                                val file = (offset.x / squareSize).toInt().coerceIn(0, 7)
                                val rank = 7 - (offset.y / squareSize).toInt().coerceIn(0, 7)
                                
                                selectedPiece = pieces.find { it.position == Pair(file, rank) && (freeMove || it.isWhite == playerIsWhite) }
                                if (selectedPiece != null) {
                                    startDragPosition = offset
                                    fingerPosition = offset
                                    possibleMoves = if (freeMove) emptyList() else calculatePossibleMoves(selectedPiece!!, pieces, gameState)
                                } else {
                                    possibleMoves = emptyList()
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            if (selectedPiece != null) {
                                dragOffset += dragAmount
                                fingerPosition += dragAmount
                                change.consume()
                            }
                        },
                        onDragEnd = { 
                            selectedPiece?.let { piece ->
                                val squareSize = size.width.toFloat() / 8f
                                val boardWidth = size.width.toFloat()
                                val boardHeight = size.height.toFloat()

                                // Vérifier si le doigt est hors du plateau à la fin du drag
                                val isOutsideBoard = fingerPosition.x < 0 || fingerPosition.x >= boardWidth ||
                                                    fingerPosition.y < 0 || fingerPosition.y >= boardHeight

                                if (freeMove && isOutsideBoard) {
                                    // Mode libre et pièce glissée hors du plateau : supprimer la pièce
                                    val updatedPieces = pieces.filterNot { it == piece }
                                    pieces = updatedPieces
                                    
                                    // Générer le nouveau FEN sans la pièce
                                    val updatedGameState = gameState // L'état du jeu ne change pas vraiment en mode setup
                                    val newFen = generateFEN(updatedPieces, isWhiteTurn, updatedGameState)
                                    onMove?.invoke(newFen)
                                } else {
                                    // Logique existante pour placer la pièce sur une case (ou la remettre si invalide)
                                    val newFile = (fingerPosition.x / squareSize).toInt().coerceIn(0, 7)
                                    val newRank = 7 - (fingerPosition.y / squareSize).toInt().coerceIn(0, 7)
                                    
                                    val fromPosition = piece.position
                                    val toPosition = Pair(newFile, newRank)
                                    
                                    // En mode libre, on place toujours la pièce si elle est sur le plateau
                                    // Sauf si on essaie de la poser sur sa propre case (pas de changement)
                                    if (fromPosition != toPosition) {
                                        val isTargetOccupiedBySameColor = pieces.any { it.position == toPosition && it.isWhite == piece.isWhite }
                                        if (freeMove && !isTargetOccupiedBySameColor) {
                                            // Placer la pièce, en supprimant celle qui est éventuellement sur la case cible
                                            val updatedPieces = pieces.filterNot { it.position == toPosition || it == piece } + piece.copy(position = toPosition)
                                            pieces = updatedPieces
                                            val updatedGameState = gameState // L'état du jeu ne change pas vraiment en mode setup
                                            val newFen = generateFEN(updatedPieces, isWhiteTurn, updatedGameState)
                                            onMove?.invoke(newFen)
                                        } else if (!freeMove && possibleMoves.contains(toPosition)) {
                                            // Logique de jeu normale (déjà existante)
                                            // ... (code pour enPassant, castling, promotion, etc.) ...
                                            // ... (mise à jour de pieces et appel à onMove(newFen)) ...
                                            val isEnPassant = piece.type == 'P' && 
                                                            toPosition == gameState.enPassantTarget &&
                                                            fromPosition.first != toPosition.first
                                            
                                            val capturedPawnPos = if (isEnPassant) {
                                                Pair(toPosition.first, fromPosition.second)
                                            } else null
                                            
                                            val isCastling = piece.type == 'K' && Math.abs(toPosition.first - fromPosition.first) == 2
                                            val rookFromPos = if (isCastling) {
                                                val rookFile = if (toPosition.first > fromPosition.first) 7 else 0
                                                Pair(rookFile, fromPosition.second)
                                            } else null
                                            val rookToPos = if (isCastling) {
                                                val rookFile = if (toPosition.first > fromPosition.first) 
                                                                fromPosition.first + 1 else fromPosition.first - 1
                                                Pair(rookFile, fromPosition.second)
                                            } else null
                                            
                                            val isPromotion = piece.type == 'P' && 
                                                            (toPosition.second == 7 && piece.isWhite || 
                                                             toPosition.second == 0 && !piece.isWhite)
                                            
                                            if (isPromotion) {
                                                pendingPromotion = Triple(fromPosition, toPosition, piece)
                                                showPromotionDialog = true
                                            } else {
                                                val updatedPieces = pieces.map { p ->
                                                    when {
                                                        p == piece -> p.copy(position = toPosition)
                                                        p.position == toPosition -> p.copy(position = Pair(-1, -1))
                                                        p.position == capturedPawnPos -> p.copy(position = Pair(-1, -1))
                                                        p.position == rookFromPos -> p.copy(position = rookToPos!!)
                                                        else -> p
                                                    }
                                                }.filter { it.position.first >= 0 }
                                                
                                                pieces = updatedPieces
                                                
                                                val updatedGameState = gameState.afterMove(fromPosition, toPosition, piece, updatedPieces)
                                                val newFen = generateFEN(updatedPieces, !isWhiteTurn, updatedGameState)
                                                onMove?.invoke(newFen)
                                            }
                                        }
                                    }
                                }
                            }
                            // Réinitialisation de l'état du drag
                            selectedPiece = null
                            dragOffset = Offset.Zero
                            startDragPosition = Offset.Zero
                            fingerPosition = Offset.Zero
                            possibleMoves = emptyList()
                        }
                    )
                }
        ) {
            val squareSize = size.width / 8f

            // Dessiner l'échiquier
            for (i in 0..7) {
                for (j in 0..7) {
                    val isLightSquare = (i + j) % 2 == 0
                    val position = Pair(i, 7 - j)
                    
                    val baseColor = if (isLightSquare) colorScheme.onSurface else colorScheme.primary
                    val color = if (possibleMoves.contains(position)) {
                        if (isLightSquare) Color(0xFFD6F5D6) else Color(0xFF86C086)
                    } else baseColor
                    
                    val king = pieces.find { it.type == 'K' && it.isWhite == isWhiteTurn && it.position == position }
                    val finalColor = if (king != null && isInCheck) {
                        if (isLightSquare) Color(0xFFF5D6D6) else Color(0xFFC08686)
                    } else color
                    
                    drawRect(
                        color = finalColor,
                        topLeft = Offset(i * squareSize, j * squareSize),
                        size = Size(squareSize, squareSize)
                    )
                }
            }

            // Dessiner les pièces
            pieces.forEach { piece ->
                if (piece != selectedPiece) {
                    drawPiece(piece, squareSize)
                }
            }

            // Dessiner la pièce sélectionnée
            selectedPiece?.let { piece ->
                val squareCenterOffset = squareSize / 2
                drawPiece(piece, squareSize, Offset(
                    x = fingerPosition.x - squareCenterOffset, 
                    y = fingerPosition.y - squareCenterOffset
                ))
            }
        }
    }
}

@Composable
fun PromotionDialog(
    isWhite: Boolean,
    onPieceSelected: (Char) -> Unit
) {
    val promotionPieces = listOf('Q', 'R', 'N', 'B')
    
    Dialog(onDismissRequest = { onPieceSelected('Q') }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choisissez une pièce",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                promotionPieces.forEach { pieceType ->
                    Button(
                        onClick = { onPieceSelected(pieceType) },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = when (pieceType) {
                                'Q' -> if (isWhite) "♕" else "♛"
                                'R' -> if (isWhite) "♖" else "♜"
                                'N' -> if (isWhite) "♘" else "♞"
                                'B' -> if (isWhite) "♗" else "♝"
                                else -> ""
                            },
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

private fun calculatePossibleMoves(
    piece: ChessPiece, 
    allPieces: List<ChessPiece>,
    gameState: ChessGameState
): List<Pair<Int, Int>> {
    val possibleMoves = mutableListOf<Pair<Int, Int>>()
    
    for (file in 0..7) {
        for (rank in 0..7) {
            val targetPosition = Pair(file, rank)
            if (ChessRules.isMoveLegal(piece.position, targetPosition, piece, allPieces, gameState)) {
                possibleMoves.add(targetPosition)
            }
        }
    }
    
    return possibleMoves
}

private fun extractTurnFromFEN(fen: String): Boolean {
    val fenParts = fen.split(" ")
    return if (fenParts.size > 1) fenParts[1] == "w" else true
}

fun parseFEN(fen: String): List<ChessPiece> {
    val pieces = mutableListOf<ChessPiece>()
    val fenBoard = fen.split(" ")[0]
    val ranks = fenBoard.split("/")
    
    ranks.forEachIndexed { rankIndex, rank ->
        var fileIndex = 0
        rank.forEach { char ->
            if (char.isDigit()) {
                fileIndex += char.toString().toInt()
            } else {
                pieces.add(
                    ChessPiece(
                        type = char.uppercaseChar(),
                        isWhite = char.isUpperCase(),
                        position = Pair(fileIndex, 7 - rankIndex)
                    )
                )
                fileIndex++
            }
        }
    }
    return pieces
}

private fun DrawScope.drawPiece(piece: ChessPiece, squareSize: Float, offset: Offset? = null) {
    val x = offset?.x ?: (piece.position.first * squareSize)
    val y = offset?.y ?: ((7 - piece.position.second) * squareSize)
    
    with(ChessPieceDrawer) {
        drawPiece(piece, x, y, squareSize)
    }
}

fun generateFEN(
    pieces: List<ChessPiece>,
    isWhiteTurn: Boolean,
    gameState: ChessGameState
): String {
    val board = Array(8) { Array(8) { ' ' } }
    
    pieces.forEach { piece ->
        val x = piece.position.first
        val y = piece.position.second
        if (x in 0..7 && y in 0..7) {
            val pieceChar = if (piece.isWhite) piece.type else piece.type.lowercaseChar()
            board[7 - y][x] = pieceChar
        }
    }
    
    val fenBoard = StringBuilder()
    for (y in 0..7) {
        var emptyCount = 0
        for (x in 0..7) {
            if (board[y][x] == ' ') {
                emptyCount++
            } else {
                if (emptyCount > 0) {
                    fenBoard.append(emptyCount)
                    emptyCount = 0
                }
                fenBoard.append(board[y][x])
            }
        }
        if (emptyCount > 0) {
            fenBoard.append(emptyCount)
        }
        if (y < 7) fenBoard.append('/')
    }
    
    val turn = if (isWhiteTurn) 'w' else 'b'
    
    var castlingRights = ""
    if (gameState.whiteCanCastleKingside) castlingRights += "K"
    if (gameState.whiteCanCastleQueenside) castlingRights += "Q"
    if (gameState.blackCanCastleKingside) castlingRights += "k"
    if (gameState.blackCanCastleQueenside) castlingRights += "q"
    if (castlingRights.isEmpty()) castlingRights = "-"
    
    val enPassant = gameState.enPassantTarget?.let {
        val file = 'a' + it.first
        val rank = it.second + 1
        "$file$rank"
    } ?: "-"
    
    return "$fenBoard $turn $castlingRights $enPassant ${gameState.halfMoveClock} ${gameState.fullMoveNumber}"
} 
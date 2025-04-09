package com.example.pikboard.chess

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp

object ChessPieceDrawer {
    private val paint = Paint().asFrameworkPaint().apply {
        textAlign = android.graphics.Paint.Align.CENTER
        color = android.graphics.Color.BLACK
    }

    // Caractères Unicode pleins
    val pieceCharsFilled = mapOf(
        'K' to "♚",
        'Q' to "♛",
        'R' to "♜",
        'B' to "♝",
        'N' to "♞",
        'P' to "♟"
    )

    // Caractères Unicode non pleins
    val pieceCharsOutline = mapOf(
        'K' to "♔",
        'Q' to "♕",
        'R' to "♖",
        'B' to "♗",
        'N' to "♘",
        'P' to "♙"
    )

    fun DrawScope.drawPiece(piece: ChessPiece, x: Float, y: Float, squareSize: Float) {
        val pieceCharFilled = pieceCharsFilled[piece.type] ?: return
        val pieceCharOutline = pieceCharsOutline[piece.type] ?: return
        
        paint.textSize = squareSize * 0.75f

        if (piece.isWhite) {
            // Pour les pièces blanches
            // D'abord dessiner le caractère plein en blanc
            paint.color = android.graphics.Color.WHITE
            drawContext.canvas.nativeCanvas.drawText(
                pieceCharFilled,
                x + squareSize / 2,
                y + squareSize * 0.7f,
                paint
            )
            
            // Puis dessiner le caractère non plein en noir par-dessus
            paint.color = android.graphics.Color.BLACK
            drawContext.canvas.nativeCanvas.drawText(
                pieceCharOutline,
                x + squareSize / 2,
                y + squareSize * 0.7f,
                paint
            )
        } else {
            // Pour les pièces noires
            // D'abord dessiner le caractère plein en noir
            paint.color = android.graphics.Color.BLACK
            drawContext.canvas.nativeCanvas.drawText(
                pieceCharFilled,
                x + squareSize / 2,
                y + squareSize * 0.7f,
                paint
            )
            
            // Puis dessiner le caractère non plein en blanc par-dessus
            paint.color = android.graphics.Color.WHITE
            drawContext.canvas.nativeCanvas.drawText(
                pieceCharOutline,
                x + squareSize / 2,
                y + squareSize * 0.7f,
                paint
            )
        }
    }
} 
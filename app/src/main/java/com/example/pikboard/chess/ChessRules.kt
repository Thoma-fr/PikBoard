package com.example.pikboard.chess

object ChessRules {
    /**
     * Vérifie si un mouvement est légal selon les règles des échecs
     */
    fun isMoveLegal(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        piece: ChessPiece,
        allPieces: List<ChessPiece>,
        gameState: ChessGameState
    ): Boolean {
        // Si la position n'a pas changé, ce n'est pas un mouvement
        if (from == to) return false
        
        // Vérifier si la case de destination contient déjà une pièce de la même couleur
        val pieceAtDestination = allPieces.find { it.position == to }
        if (pieceAtDestination != null && pieceAtDestination.isWhite == piece.isWhite) {
            return false // Ne peut pas capturer sa propre pièce
        }

        // Calculer les déplacements en file et en rang
        val deltaFile = to.first - from.first
        val deltaRank = to.second - from.second

        // Vérifier selon le type de pièce
        val isValidBasicMove = when (piece.type) {
            'P' -> isValidPawnMove(from, to, deltaFile, deltaRank, piece.isWhite, allPieces, gameState)
            'R' -> isValidRookMove(from, to, deltaFile, deltaRank, allPieces)
            'N' -> isValidKnightMove(deltaFile, deltaRank)
            'B' -> isValidBishopMove(from, to, deltaFile, deltaRank, allPieces)
            'Q' -> isValidQueenMove(from, to, deltaFile, deltaRank, allPieces)
            'K' -> isValidKingMove(from, to, deltaFile, deltaRank, allPieces, gameState, piece.isWhite)
            else -> false
        }
        
        // Si le mouvement de base n'est pas valide, on arrête là
        if (!isValidBasicMove) return false
        
        // Vérifier que le mouvement ne met pas/laisse pas notre roi en échec
        return !wouldBeInCheck(from, to, piece, allPieces)
    }

    /**
     * Vérifie si un mouvement de pion est valide
     */
    private fun isValidPawnMove(
        from: Pair<Int, Int>, 
        to: Pair<Int, Int>, 
        deltaFile: Int, 
        deltaRank: Int, 
        isWhite: Boolean, 
        allPieces: List<ChessPiece>,
        gameState: ChessGameState
    ): Boolean {
        val direction = if (isWhite) 1 else -1
        val startRank = if (isWhite) 1 else 6
        
        // Capture en diagonale
        if (Math.abs(deltaFile) == 1) {
            // Capture normale
            if (deltaRank == direction) {
                val validCapture = allPieces.any { 
                    it.position == to && it.isWhite != isWhite 
                }
                
                // Prise en passant
                val enPassantTarget = gameState.enPassantTarget
                val isEnPassant = to == enPassantTarget && 
                                 from.second == (if (isWhite) 4 else 3)
                
                return validCapture || isEnPassant
            }
            return false
        }
        
        // Avance d'une case
        if (deltaFile == 0 && deltaRank == direction) {
            return allPieces.none { it.position == to }
        }
        
        // Avance de deux cases depuis la position de départ
        if (deltaFile == 0 && deltaRank == 2 * direction && from.second == startRank) {
            val intermediatePosRank = from.second + direction
            val intermediatePos = Pair(from.first, intermediatePosRank)
            return allPieces.none { it.position == to || it.position == intermediatePos }
        }
        
        return false
    }

    /**
     * Vérifie si un mouvement de tour est valide
     */
    private fun isValidRookMove(
        from: Pair<Int, Int>, 
        to: Pair<Int, Int>, 
        deltaFile: Int, 
        deltaRank: Int, 
        allPieces: List<ChessPiece>
    ): Boolean {
        // La tour se déplace horizontalement ou verticalement
        if (deltaFile != 0 && deltaRank != 0) {
            return false
        }
        
        // Vérifier qu'il n'y a pas de pièces sur le chemin
        return !hasObstaclesInPath(from, to, allPieces)
    }

    /**
     * Vérifie si un mouvement de cavalier est valide
     */
    private fun isValidKnightMove(deltaFile: Int, deltaRank: Int): Boolean {
        // Le cavalier se déplace en L
        return (Math.abs(deltaFile) == 1 && Math.abs(deltaRank) == 2) || 
               (Math.abs(deltaFile) == 2 && Math.abs(deltaRank) == 1)
    }

    /**
     * Vérifie si un mouvement de fou est valide
     */
    private fun isValidBishopMove(
        from: Pair<Int, Int>, 
        to: Pair<Int, Int>, 
        deltaFile: Int, 
        deltaRank: Int, 
        allPieces: List<ChessPiece>
    ): Boolean {
        // Le fou se déplace en diagonale
        if (Math.abs(deltaFile) != Math.abs(deltaRank)) {
            return false
        }
        
        // Vérifier qu'il n'y a pas de pièces sur le chemin
        return !hasObstaclesInPath(from, to, allPieces)
    }

    /**
     * Vérifie si un mouvement de dame est valide
     */
    private fun isValidQueenMove(
        from: Pair<Int, Int>, 
        to: Pair<Int, Int>, 
        deltaFile: Int, 
        deltaRank: Int, 
        allPieces: List<ChessPiece>
    ): Boolean {
        // La dame se déplace comme une tour ou un fou
        val isDiagonal = Math.abs(deltaFile) == Math.abs(deltaRank)
        val isStraight = deltaFile == 0 || deltaRank == 0
        
        if (!isDiagonal && !isStraight) {
            return false
        }
        
        // Vérifier qu'il n'y a pas de pièces sur le chemin
        return !hasObstaclesInPath(from, to, allPieces)
    }

    /**
     * Vérifie si un mouvement de roi est valide
     */
    private fun isValidKingMove(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        deltaFile: Int,
        deltaRank: Int,
        allPieces: List<ChessPiece>,
        gameState: ChessGameState,
        isWhite: Boolean
    ): Boolean {
        // Déplacement normal d'une case
        if (Math.abs(deltaFile) <= 1 && Math.abs(deltaRank) <= 1) {
            return true
        }
        
        // Vérifier le roque
        if (deltaRank == 0 && Math.abs(deltaFile) == 2) {
            // Le roi ne doit pas avoir bougé
            val canCastleKingside = if (isWhite) gameState.whiteCanCastleKingside else gameState.blackCanCastleKingside
            val canCastleQueenside = if (isWhite) gameState.whiteCanCastleQueenside else gameState.blackCanCastleQueenside
            
            // Roque côté roi (petit roque)
            if (deltaFile == 2 && canCastleKingside) {
                val rookFile = 7
                val rookRank = from.second
                val rookPos = Pair(rookFile, rookRank)
                
                // Vérifier que la tour est présente
                val rook = allPieces.find { it.position == rookPos && it.type == 'R' && it.isWhite == isWhite }
                if (rook == null) return false
                
                // Vérifier qu'il n'y a pas de pièces entre le roi et la tour
                if (hasObstaclesInPath(from, rookPos, allPieces)) return false
                
                // Vérifier que le roi ne passe pas par une case attaquée
                val intermediatePos = Pair(from.first + 1, from.second)
                return !isSquareAttacked(from, allPieces, !isWhite) && 
                       !isSquareAttacked(intermediatePos, allPieces, !isWhite) && 
                       !isSquareAttacked(to, allPieces, !isWhite)
            }
            
            // Roque côté dame (grand roque)
            if (deltaFile == -2 && canCastleQueenside) {
                val rookFile = 0
                val rookRank = from.second
                val rookPos = Pair(rookFile, rookRank)
                
                // Vérifier que la tour est présente
                val rook = allPieces.find { it.position == rookPos && it.type == 'R' && it.isWhite == isWhite }
                if (rook == null) return false
                
                // Vérifier qu'il n'y a pas de pièces entre le roi et la tour
                if (hasObstaclesInPath(from, rookPos, allPieces)) return false
                
                // Vérifier que le roi ne passe pas par une case attaquée
                val intermediatePos = Pair(from.first - 1, from.second)
                return !isSquareAttacked(from, allPieces, !isWhite) && 
                       !isSquareAttacked(intermediatePos, allPieces, !isWhite) && 
                       !isSquareAttacked(to, allPieces, !isWhite)
            }
        }
        
        return false
    }

    /**
     * Vérifie s'il y a des obstacles sur le chemin entre deux positions
     */
    private fun hasObstaclesInPath(
        from: Pair<Int, Int>, 
        to: Pair<Int, Int>, 
        allPieces: List<ChessPiece>
    ): Boolean {
        val deltaFile = to.first - from.first
        val deltaRank = to.second - from.second
        
        // Déterminer la direction du mouvement
        val fileStep = when {
            deltaFile > 0 -> 1
            deltaFile < 0 -> -1
            else -> 0
        }
        
        val rankStep = when {
            deltaRank > 0 -> 1
            deltaRank < 0 -> -1
            else -> 0
        }
        
        var currentFile = from.first + fileStep
        var currentRank = from.second + rankStep
        
        // Parcourir toutes les cases entre from et to (exclusif)
        while (currentFile != to.first || currentRank != to.second) {
            if (allPieces.any { it.position == Pair(currentFile, currentRank) }) {
                return true // Obstacle trouvé
            }
            
            currentFile += fileStep
            currentRank += rankStep
        }
        
        return false // Pas d'obstacle
    }
    
    /**
     * Vérifie si une case est attaquée par une pièce de la couleur spécifiée
     */
    fun isSquareAttacked(
        square: Pair<Int, Int>,
        allPieces: List<ChessPiece>,
        byWhite: Boolean
    ): Boolean {
        // Vérifier si une pièce de la couleur spécifiée peut atteindre la case
        return allPieces.filter { it.isWhite == byWhite }.any { piece ->
            val deltaFile = square.first - piece.position.first
            val deltaRank = square.second - piece.position.second
            
            when (piece.type) {
                'P' -> {
                    val direction = if (byWhite) 1 else -1
                    Math.abs(deltaFile) == 1 && deltaRank == direction
                }
                'R' -> {
                    (deltaFile == 0 || deltaRank == 0) && 
                    !hasObstaclesInPath(piece.position, square, allPieces)
                }
                'N' -> {
                    (Math.abs(deltaFile) == 1 && Math.abs(deltaRank) == 2) || 
                    (Math.abs(deltaFile) == 2 && Math.abs(deltaRank) == 1)
                }
                'B' -> {
                    Math.abs(deltaFile) == Math.abs(deltaRank) && 
                    !hasObstaclesInPath(piece.position, square, allPieces)
                }
                'Q' -> {
                    ((deltaFile == 0 || deltaRank == 0) || Math.abs(deltaFile) == Math.abs(deltaRank)) && 
                    !hasObstaclesInPath(piece.position, square, allPieces)
                }
                'K' -> {
                    Math.abs(deltaFile) <= 1 && Math.abs(deltaRank) <= 1
                }
                else -> false
            }
        }
    }
    
    /**
     * Vérifie si le roi de la couleur spécifiée est en échec
     */
    fun isInCheck(allPieces: List<ChessPiece>, isWhiteKing: Boolean): Boolean {
        // Trouver la position du roi
        val king = allPieces.find { it.type == 'K' && it.isWhite == isWhiteKing } ?: return false
        
        // Vérifier si le roi est attaqué
        return isSquareAttacked(king.position, allPieces, !isWhiteKing)
    }
    
    /**
     * Vérifie si un mouvement mettrait ou laisserait le roi en échec
     */
    private fun wouldBeInCheck(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        piece: ChessPiece,
        allPieces: List<ChessPiece>
    ): Boolean {
        // Simuler le mouvement
        val simulatedPieces = allPieces.map { 
            when {
                it.position == from -> it.copy(position = to)
                it.position == to -> it.copy(position = Pair(-1, -1)) // Capture
                else -> it
            }
        }.filter { it.position.first >= 0 } // Retirer les pièces capturées
        
        // Vérifier si le roi est en échec après le mouvement
        return isInCheck(simulatedPieces, piece.isWhite)
    }
    
    /**
     * Vérifie si le joueur est en échec et mat
     */
    fun isCheckmate(allPieces: List<ChessPiece>, isWhiteToMove: Boolean, gameState: ChessGameState): Boolean {
        // Vérifier d'abord si le roi est en échec
        if (!isInCheck(allPieces, isWhiteToMove)) return false
        
        // Pour chaque pièce de la couleur qui joue
        val playerPieces = allPieces.filter { it.isWhite == isWhiteToMove }
        
        // Vérifier s'il existe au moins un mouvement légal
        return playerPieces.none { piece ->
            // Pour chaque case de l'échiquier
            (0..7).flatMap { file ->
                (0..7).map { rank ->
                    isMoveLegal(piece.position, Pair(file, rank), piece, allPieces, gameState)
                }
            }.any { it } // Y a-t-il au moins un mouvement légal ?
        }
    }
    
    /**
     * Vérifie si une pièce peut être promue
     */
    fun canPromote(piece: ChessPiece): Boolean {
        if (piece.type != 'P') return false
        
        val promotionRank = if (piece.isWhite) 7 else 0
        return piece.position.second == promotionRank
    }
}

/**
 * Classe pour stocker l'état actuel du jeu
 */
data class ChessGameState(
    // Pour le roque
    val whiteCanCastleKingside: Boolean = true,
    val whiteCanCastleQueenside: Boolean = true,
    val blackCanCastleKingside: Boolean = true,
    val blackCanCastleQueenside: Boolean = true,
    
    // Pour la prise en passant
    val enPassantTarget: Pair<Int, Int>? = null,
    
    // Pour la règle des 50 coups
    val halfMoveClock: Int = 0,
    
    // Numéro du coup
    val fullMoveNumber: Int = 1
) {
    companion object {
        /**
         * Crée un état de jeu à partir d'une chaîne FEN
         */
        fun fromFEN(fen: String): ChessGameState {
            val parts = fen.split(" ")
            if (parts.size < 4) return ChessGameState() // FEN par défaut si incomplet
            
            // Extraire les informations de roque (3ème élément)
            val castlingRights = parts[2]
            val whiteCanCastleKingside = castlingRights.contains('K')
            val whiteCanCastleQueenside = castlingRights.contains('Q')
            val blackCanCastleKingside = castlingRights.contains('k')
            val blackCanCastleQueenside = castlingRights.contains('q')
            
            // Extraire la cible de prise en passant (4ème élément)
            val enPassantTarget = if (parts[3] != "-") {
                val file = parts[3][0] - 'a'
                val rank = parts[3][1] - '1'
                Pair(file, rank)
            } else null
            
            // Extraire le compteur de demi-coups pour la règle des 50 coups (5ème élément)
            val halfMoveClock = if (parts.size > 4) parts[4].toIntOrNull() ?: 0 else 0
            
            // Extraire le numéro du coup (6ème élément)
            val fullMoveNumber = if (parts.size > 5) parts[5].toIntOrNull() ?: 1 else 1
            
            return ChessGameState(
                whiteCanCastleKingside,
                whiteCanCastleQueenside,
                blackCanCastleKingside,
                blackCanCastleQueenside,
                enPassantTarget,
                halfMoveClock,
                fullMoveNumber
            )
        }
    }
    
    /**
     * Génère un nouvel état après un mouvement
     */
    fun afterMove(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        piece: ChessPiece,
        allPieces: List<ChessPiece>
    ): ChessGameState {
        val isWhite = piece.isWhite
        
        // Mettre à jour le droit au roque si le roi ou une tour bouge
        var newWhiteCanCastleKingside = whiteCanCastleKingside
        var newWhiteCanCastleQueenside = whiteCanCastleQueenside
        var newBlackCanCastleKingside = blackCanCastleKingside
        var newBlackCanCastleQueenside = blackCanCastleQueenside
        
        if (piece.type == 'K') {
            if (isWhite) {
                newWhiteCanCastleKingside = false
                newWhiteCanCastleQueenside = false
            } else {
                newBlackCanCastleKingside = false
                newBlackCanCastleQueenside = false
            }
        } else if (piece.type == 'R') {
            // Tour du côté roi
            if (from == Pair(7, if (isWhite) 0 else 7)) {
                if (isWhite) newWhiteCanCastleKingside = false
                else newBlackCanCastleKingside = false
            }
            // Tour du côté dame
            else if (from == Pair(0, if (isWhite) 0 else 7)) {
                if (isWhite) newWhiteCanCastleQueenside = false
                else newBlackCanCastleQueenside = false
            }
        }
        
        // Vérifier si une tour est capturée (perte du droit au roque)
        val capturedPiece = allPieces.find { it.position == to }
        if (capturedPiece?.type == 'R') {
            if (to == Pair(7, 7)) newBlackCanCastleKingside = false
            else if (to == Pair(0, 7)) newBlackCanCastleQueenside = false
            else if (to == Pair(7, 0)) newWhiteCanCastleKingside = false
            else if (to == Pair(0, 0)) newWhiteCanCastleQueenside = false
        }
        
        // Mettre à jour la cible de prise en passant si un pion avance de deux cases
        val newEnPassantTarget = if (piece.type == 'P' && Math.abs(to.second - from.second) == 2) {
            val enPassantRank = (from.second + to.second) / 2
            Pair(from.first, enPassantRank)
        } else null
        
        // Mettre à jour le compteur de demi-coups (pour la règle des 50 coups)
        val isCapture = capturedPiece != null
        val isPawnMove = piece.type == 'P'
        val newHalfMoveClock = if (isCapture || isPawnMove) 0 else halfMoveClock + 1
        
        // Mettre à jour le numéro du coup
        val newFullMoveNumber = if (!isWhite) fullMoveNumber + 1 else fullMoveNumber
        
        return ChessGameState(
            newWhiteCanCastleKingside,
            newWhiteCanCastleQueenside,
            newBlackCanCastleKingside,
            newBlackCanCastleQueenside,
            newEnPassantTarget,
            newHalfMoveClock,
            newFullMoveNumber
        )
    }
} 
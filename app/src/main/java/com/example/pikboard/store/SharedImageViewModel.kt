package com.example.pikboard.store

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pikboard.api.UserApi

class SharedImageViewModel : ViewModel() {
    var selectedImageBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var pcurrentFen by mutableStateOf<String>("")

    var currentOpponentChess by mutableStateOf<UserApi?>(null)
        private set

    var currentGameID by mutableStateOf<Int?>(null)
        private set

    var currentGameWhitePlayer by mutableStateOf<Int?>(null)
        private set

    var userID by mutableStateOf<Int?>(null)
        private set

    fun setImageBitmap(bitmap: Bitmap) {
        selectedImageBitmap = bitmap
    }

    fun setCurrentFenP(fen: String) {
        pcurrentFen = fen
    }

    fun setCurrentOpponent(opponent: UserApi?) {
        currentOpponentChess = opponent
    }

    fun setCurrentGameID(gameID: Int) {
        currentGameID = gameID
    }

    fun updateGameWhitePlayer(whiteID: Int?) {
        currentGameWhitePlayer = whiteID
    }

    fun updateUserID(id: Int?) {
        userID = id
    }
}
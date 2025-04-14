package com.example.pikboard.api.databaseFirebase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChessGameViewModel : ViewModel() {

    private val database = Firebase.database("https://pikboard-28b16-default-rtdb.firebaseio.com")

    val game = mutableStateOf<ChessGame?>(null)

    fun observeGameById(id: Int) {
        database.getReference("games/$id")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val gameDb = snapshot.getValue(ChessGame::class.java)
                    if (gameDb != null) {
                        game.value = gameDb
                        Log.d("ChessGameViewModel", "Game $id updated: $gameDb")
                    } else {
                        Log.w("ChessGameViewModel", "Game $id not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChessGameViewModel", "Observe cancelled: ${error.message}")
                }
            })
    }

    fun createGameWithId(game: ChessGame) {
        val gameRef = database.getReference("games/${game.id}")
        gameRef.setValue(game)
            .addOnSuccessListener {
                Log.d("ChessGameViewModel", "Game ${game.id} created successfully")
            }
            .addOnFailureListener { error ->
                Log.e("ChessGameViewModel", "Error creating game: ${error.message}")
            }
    }

    fun updateBoardById(id: Int, newBoard: String) {
        val boardRef = database.getReference("games/$id/board")
        boardRef.setValue(newBoard)
            .addOnSuccessListener {
                Log.d("ChessGameViewModel", "Board for game $id updated to: $newBoard")
            }
            .addOnFailureListener { error ->
                Log.e("ChessGameViewModel", "Error updating board: ${error.message}")
            }
    }
}
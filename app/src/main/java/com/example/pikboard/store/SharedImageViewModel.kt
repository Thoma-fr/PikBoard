package com.example.pikboard.store

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedImageViewModel : ViewModel() {
    var selectedImageBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var pcurrentFen by mutableStateOf<String>("")
        private set

    fun setImageBitmap(bitmap: Bitmap) {
        selectedImageBitmap = bitmap
    }

    fun setCurrentFenP(fen: String) {
        pcurrentFen = fen
    }
}
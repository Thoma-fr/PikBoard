package com.example.pikboard.store

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedImageViewModel : ViewModel() {
    var selectedImageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    fun setImageBitmap(bitmap: Bitmap) {
        selectedImageBitmap = bitmap
    }
}
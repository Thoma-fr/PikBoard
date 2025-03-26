package com.example.pikboard.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pikboard.store.User
import com.example.pikboard.store.UserDao
import com.example.pikboard.store.readSessionToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfilePage(userDao: UserDao) {
    val context = LocalContext.current
    val token by readSessionToken(context).collectAsState(initial = "")

    val user by userDao.getUserById(0).collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is the ProfilePage page $token")
        if (user != null) {
            Text(text = "Username: ${user!!.username}")
            Text(text = "Email: ${user!!.email}")
        } else {
            Text(text = "Chargement des informations utilisateur...")
        }    }
}

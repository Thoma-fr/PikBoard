package com.example.pikboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.ui.screens.LoginScreen
import com.example.pikboard.ui.screens.Routes
import com.example.pikboard.ui.screens.SignupScreen
import com.example.pikboard.ui.theme.PikBoardTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PikBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    PikBoardApp(innerPadding)
                }
            }
        }
    }

    @Composable
    fun PikBoardApp(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.LOGIN_PAGE){
            composable(Routes.LOGIN_PAGE) {
                LoginScreen(innerPadding)
            }

            composable(Routes.SIGNUP_PAGE) {
                SignupScreen()
            }
        }
    }

}


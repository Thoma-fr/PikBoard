package com.example.pikboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
                PikBoardApp()
            }
        }
    }

    @Composable
    fun PikBoardApp() {
        var navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.LOGIN_PAGE){
            composable(Routes.LOGIN_PAGE) {
                LoginScreen()
            }

            composable(Routes.SIGNUP_PAGE) {
                SignupScreen()
            }
        }
    }

}


package com.example.pikboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.ui.Fragment.PikNavBar
import com.example.pikboard.ui.screens.HomeScreen
import com.example.pikboard.ui.screens.auth.LoginScreen
import com.example.pikboard.ui.screens.Routes
import com.example.pikboard.ui.screens.auth.SignupScreen
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
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Scaffold(
            bottomBar = {
                if (currentRoute != Routes.Auth.LOGIN_PAGE &&
                    currentRoute != Routes.Auth.SIGNUP_PAGE) {
                    PikNavBar()
                }

            }
        ) { scaffoldPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Auth.LOGIN_PAGE,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                composable(Routes.Auth.LOGIN_PAGE) {
                    LoginScreen(navController)
                }
                composable(Routes.Auth.SIGNUP_PAGE) {
                    SignupScreen(navController)
                }
                composable(Routes.HOME_PAGE) {
                    HomeScreen(navController)
                }
            }
        }
    }
}


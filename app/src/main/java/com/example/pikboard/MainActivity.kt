package com.example.pikboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.ui.Fragment.PikNavBar
import com.example.pikboard.ui.screens.game.AddGamePage
import com.example.pikboard.ui.screens.FriendsScreen
import com.example.pikboard.ui.screens.game.GameScreen
import com.example.pikboard.ui.screens.HomeScreen
import com.example.pikboard.ui.screens.ProfilePage
import com.example.pikboard.ui.screens.auth.LoginScreen
import com.example.pikboard.ui.screens.Routes
import com.example.pikboard.ui.screens.auth.SignupScreen
import com.example.pikboard.ui.theme.PikBoardTheme
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pikboard.store.SharedImageViewModel
import com.example.pikboard.ui.screens.EditProfileScreen
import com.example.pikboard.ui.screens.game.GameFriendSreen
import com.example.pikboard.ui.screens.game.GamePreviewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PikBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PikBoardApp(innerPadding)
                }
            }
        }
    }

    @Composable
    fun PikBoardApp(innerPadding: PaddingValues) {
        val pikBoardApiViewModel = ViewModelProvider(this)[PikBoardApiViewModel::class.java]
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val sharedViewModel: SharedImageViewModel = viewModel()


        Scaffold(
            bottomBar = {
                if (currentRoute != Routes.Auth.LOGIN_PAGE &&
                    currentRoute != Routes.Auth.SIGNUP_PAGE) {
                    PikNavBar(navController)
                }

            }
        ) { scaffoldPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Auth.LOGIN_PAGE,
                modifier = Modifier.padding(
                    top = scaffoldPadding.calculateTopPadding(),
                    start = scaffoldPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = scaffoldPadding.calculateEndPadding(LayoutDirection.Rtl)
                )
            ) {
                composable(Routes.Auth.LOGIN_PAGE) {
                    LoginScreen(navController, pikBoardApiViewModel)
                }
                composable(Routes.Auth.SIGNUP_PAGE) {
                    SignupScreen(navController, pikBoardApiViewModel)
                }
                composable(Routes.HOME_PAGE) {
                    HomeScreen(navController, sharedViewModel, pikBoardApiViewModel)
                }
                composable(Routes.FRIENDS_PAGE) {
                    FriendsScreen()
                }
                composable(Routes.PROFILE_PAGE) {
                    ProfilePage(navController, sharedViewModel, pikBoardApiViewModel)
                }
                composable(Routes.Game.NEW) {
                    AddGamePage(navController, sharedViewModel)
                }
                composable(Routes.Game.CHESS){
                    GameScreen(navController, sharedViewModel)
                }
                composable(Routes.Game.PREVIEW) {
                    GamePreviewScreen(navController, sharedViewModel, pikBoardApiViewModel)
                }
                composable(Routes.Game.FRIEND) {
                    GameFriendSreen(navController, sharedViewModel, pikBoardApiViewModel)
                }
                composable(Routes.EDIT_PROFILE) {
                    EditProfileScreen(navController, sharedViewModel, pikBoardApiViewModel)
                }
            }
        }
    }
}


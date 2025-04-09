package com.example.pikboard.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.store.saveSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikPasswordField
import com.example.pikboard.ui.Fragment.PikTextField
import com.example.pikboard.ui.screens.Routes
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(navController: NavHostController, pikBoardApiViewModel: PikBoardApiViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf("") }
    var apiErrorMessage by remember { mutableStateOf("") }
    var isSignupApiCallLoading by remember { mutableStateOf(false) }

    val signupResult = pikBoardApiViewModel.signupResponse.observeAsState()
    val loginResult = pikBoardApiViewModel.loginResponse.observeAsState()

    when(val result = signupResult.value) {
        is NetworkResponse.Error -> {
            apiErrorMessage = result.message
            isSignupApiCallLoading = false
        }
        NetworkResponse.Loading -> {
            isSignupApiCallLoading = true
        }
        is NetworkResponse.Success -> {
            pikBoardApiViewModel.resetSignup()
            pikBoardApiViewModel.login(email, password)
        }
        null -> {}
    }

    when(val result = loginResult.value) {
        is NetworkResponse.Error -> {
            apiErrorMessage = result.message
            isSignupApiCallLoading = false
        }
        NetworkResponse.Loading -> {
            isSignupApiCallLoading = true
        }
        is NetworkResponse.Success -> {
            isSignupApiCallLoading = false
            scope.launch {
                saveSessionToken(context, result.data.data.token)
            }

            navController.navigate(Routes.HOME_PAGE)
        }
        null -> {}
    }


    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(text = "Welcome", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(24.dp))

        PikTextField(
            value = username,
            onValueChange = {username = it},
            hint = "Username",
            leadingIcon = Icons.Rounded.AccountCircle
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikTextField(
            value = email,
            onValueChange = {email = it},
            hint = "Email",
            leadingIcon = Icons.Rounded.Email
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikPasswordField(
            value = password,
            onValueChange = {password = it},
            hint = passwordError.ifEmpty { "Password" },
            color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified,
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikPasswordField(
            value = confPassword,
            onValueChange = {confPassword = it},
            hint = "Confirm password"
        )

        if (apiErrorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = apiErrorMessage, fontSize = 16.sp, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        PikButton("Signup", isSignupApiCallLoading) {
            if (username.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                confPassword.isNotEmpty()
            ) {
                if (confPassword == password) {
                    pikBoardApiViewModel.signup(username, email, password)
                } else {
                    apiErrorMessage = "Passwords are different"
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController(), PikBoardApiViewModel())
}

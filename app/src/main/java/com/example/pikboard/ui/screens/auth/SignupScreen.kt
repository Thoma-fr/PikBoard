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
    var userNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isSignupApiCallLoading by remember { mutableStateOf(false) }

    val signupResult = pikBoardApiViewModel.signupResponse.observeAsState()
    val loginResult = pikBoardApiViewModel.loginResponse.observeAsState()

    when(val result = signupResult.value) {
        is NetworkResponse.Error -> {
            apiErrorMessage = result.message
            if (apiErrorMessage == "User already exists"){
                userNameError = true
                emailError = false
            } else if (apiErrorMessage == "Email already exists") {
                emailError = true
                userNameError = false
            } else {
                userNameError = false
                emailError = false
            }
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
        Text(text = "to", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "PIKBOARD", fontSize = 50.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Enter your information", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(10.dp))
        PikTextField(
            value = username,
            onValueChange = {username = it},
            hint = "Username",
            color = if (userNameError) Color.Red else Color.Unspecified,
            leadingIcon = Icons.Rounded.AccountCircle
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikTextField(
            value = email,
            onValueChange = {email = it},
            hint = "Email",
            color = if (emailError) Color.Red else Color.Unspecified,
            leadingIcon = Icons.Rounded.Email
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikPasswordField(
            value = password,
            onValueChange = {password = it},
            hint = passwordError.ifEmpty { "Password" },
            color = if (passwordError.isNotEmpty() || isPasswordError) Color.Red else Color.Unspecified,
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikPasswordField(
            value = confPassword,
            onValueChange = {confPassword = it},
            color = if (isPasswordError) Color.Red else Color.Unspecified,
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
                isPasswordError = false
                if (password.length < 8) {
                    apiErrorMessage = "Password too short"
                    isPasswordError = true
                } else if (confPassword == password) {
                    pikBoardApiViewModel.signup(username, email, password)
                } else {
                    apiErrorMessage = "Passwords are different"
                    isPasswordError = true
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

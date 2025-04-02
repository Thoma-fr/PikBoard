package com.example.pikboard.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.R
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.store.saveSessionToken
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikPasswordField
import com.example.pikboard.ui.Fragment.PikTextField
import com.example.pikboard.ui.screens.Routes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: PikBoardApiViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // INFO: For read app set email = "" and password = ""
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isLoginApiCallLoading by remember { mutableStateOf(false) }

    val loginResult = viewModel.loginResponse.observeAsState()


    when(val result = loginResult.value) {
        is NetworkResponse.Error -> {
            errorMessage = "Email or Password incorrect"
            isLoginApiCallLoading = false
        }
        NetworkResponse.Loading -> {
            isLoginApiCallLoading = true
        }
        is NetworkResponse.Success -> {
            isLoginApiCallLoading = false
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

        Image(
            painter = painterResource(id = R.drawable.default_image),
            contentDescription = "",
            modifier = Modifier.height(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(24.dp))

        PikTextField(
            value = email,
            onValueChange = {email = it},
            hint = emailError.ifEmpty { "Email" } ,
            color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified,
            leadingIcon = Icons.Rounded.AccountCircle
        )

        Spacer(modifier = Modifier.height(8.dp))

        PikPasswordField(
            value = password,
            onValueChange = {password = it},
            hint = passwordError.ifEmpty { "Password" },
            color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified,
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, fontSize = 16.sp, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        PikButton("Login", isLoginApiCallLoading) {
            emailError = if (email.isBlank()) "Email is required" else ""
            passwordError = if (password.isBlank()) "Password is required" else ""
            if (emailError.isEmpty() && passwordError.isEmpty()) {
                errorMessage = ""
                viewModel.login(email, password)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot Password ? ",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Handle forgot password logic
                }
            )

        Spacer( modifier = Modifier.height(50.dp))

        Row  {
            Text(text = "Not a member ?")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Signup now !",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Auth.SIGNUP_PAGE)
                })
        }

    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController(), PikBoardApiViewModel())
}
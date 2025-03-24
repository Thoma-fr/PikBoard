package com.example.pikboard.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.R
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikPasswordField
import com.example.pikboard.ui.Fragment.PikTextField
import com.example.pikboard.ui.screens.Routes

@Composable
fun LoginScreen(paddingValues: PaddingValues, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
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

        Spacer(modifier = Modifier.height(24.dp))

        PikButton("Login") {
            emailError = if (email.isBlank()) "Email is required" else ""
            passwordError = if (password.isBlank()) "Password is required" else ""
            if (emailError.isEmpty() && passwordError.isEmpty()) {
                // TODO: Handle api call to login
                navController.navigate(Routes.HOME_PAGE)
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
    LoginScreen(PaddingValues(), rememberNavController())
}
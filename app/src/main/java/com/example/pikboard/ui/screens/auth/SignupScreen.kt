package com.example.pikboard.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikPasswordField
import com.example.pikboard.ui.Fragment.PikTextField

@Composable
fun SignupScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf("") }

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

        Spacer(modifier = Modifier.height(8.dp))

        PikButton("Signup") {
            if (username.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                confPassword.isNotEmpty() &&
                confPassword == password
            ) {
                // TODO: Handle api call to Signup
            } else {
                // TODO: IDK, show the error
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}

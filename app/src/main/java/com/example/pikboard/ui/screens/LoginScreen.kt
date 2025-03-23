package com.example.pikboard.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
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
import com.example.pikboard.R

@Composable
fun LoginScreen(paddingValues: PaddingValues) {
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

        TextField(
            value = email,
            onValueChange = {email = it},
            label = {Text(emailError.ifEmpty { "Email" }, color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon( Icons.Rounded.AccountCircle,
                    contentDescription = "")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {password = it},
            label = {Text(passwordError.ifEmpty { "Password" }, color = if(passwordError.isNotEmpty())Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon( Icons.Rounded.Lock, contentDescription = "")
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility)
                    painterResource(id = R.drawable.visibility_24px)
                else painterResource(id = R.drawable.visibility_off_24px)

                Icon(
                    painter = image,
                    contentDescription = "",
                    modifier = Modifier.clickable { passwordVisibility = !passwordVisibility }
                )
            },

            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = if (email.isBlank()) "Email is required" else ""
                passwordError = if (password.isBlank()) "Password is required" else ""
                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    // TODO: Handle api call to login
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp)
        ) { Text(text = "Login") }

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
            Text(text = "Sigup now !",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    // TODO: Redirect to login page
                })
        }

    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(PaddingValues())
}
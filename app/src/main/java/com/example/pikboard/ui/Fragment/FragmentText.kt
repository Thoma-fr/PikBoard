package com.example.pikboard.ui.Fragment

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Lock
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pikboard.R

@Composable
fun PikTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    color: Color = Color.Unspecified,
    leadingIcon: ImageVector = Icons.Rounded.Create)
{
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = hint, color = color) },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = "")
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.width(360.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
@Preview
@Composable
fun PikTextFieldPreview() {
    PikTextField(
        value = "Text value",
        onValueChange = {},
        hint = "This is the hint"
    )
}

@Composable
fun PikPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    color: Color = Color.Unspecified,
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    TextField (
    value = value,
    onValueChange = onValueChange,
    label = {Text(text = hint, color = color)},
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
            modifier = Modifier.size(24.dp).clickable { passwordVisibility = !passwordVisibility }
        )
    },
    shape = RoundedCornerShape(8.dp),
    modifier = Modifier.width(360.dp),
    colors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    ))
}
@Preview
@Composable
fun PikPasswordFieldPreview() {
    PikPasswordField(
        value = "my_password_123",
        onValueChange = {},
        hint = "Password"
    )
}

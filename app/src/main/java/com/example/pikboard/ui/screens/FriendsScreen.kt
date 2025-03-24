package com.example.pikboard.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.R
import com.example.pikboard.ui.Fragment.FriendTile
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.Fragment.PikTextField
import com.example.pikboard.ui.Fragment.Tile

@Composable
fun FriendsScreen() {
    PikHeader()
    var searchQuery by remember { mutableStateOf("") }

    Column {
        PikHeader()

        PikTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            hint = "Search...",
            color = Color.Unspecified,
            leadingIcon = Icons.Rounded.Search
        )
        Friends()
        PikButton(text="Add Friends") {  }
    }
}
@Composable
fun Friends(names: List<String> = listOf("mario", "isaac","monsterhunter","bruh","starbound","Undertale"))
{
    for (name in names) {
        FriendTile(name)

    }
}
@Preview(showBackground = true)
@Composable
fun FriendScreenPreview() {
    FriendsScreen()
}
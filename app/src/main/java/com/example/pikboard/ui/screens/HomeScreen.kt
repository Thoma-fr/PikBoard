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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.R
import com.example.pikboard.ui.Fragment.PikButton
import com.example.pikboard.ui.Fragment.PikHeader

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PikHeader(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(16.dp))

        PikButton("New Game") { /* Action */ }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Continue game")

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            items(7) { _ ->
                // TODO:
                // [ ] Faire des tuilles pour chaque partie en cours
                Image(
                    painter = painterResource(id = R.drawable.default_image),
                    contentDescription = "",
                    modifier = Modifier.height(150.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen( rememberNavController())
}
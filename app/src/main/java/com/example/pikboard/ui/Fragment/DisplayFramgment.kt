package com.example.pikboard.ui.Fragment

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pikboard.ui.screens.Routes

@Composable
fun PikHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "PikBoard",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PikHeaderPreview() {
    PikHeader()
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PikNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = Routes.HOME_PAGE
        ),
        BottomNavigationItem(
            title = "Plus",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            hasNews = false,
            route = Routes.Game.NEW
        ),
        BottomNavigationItem(
            title = "Friend",
            selectedIcon = Icons.Filled.Groups,
            unselectedIcon = Icons.Outlined.Groups,
            hasNews = false,
            route = Routes.FRIENDS_PAGE
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = false,
            route = Routes.PROFILE_PAGE
        ),
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar() {
        items.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route)
                          },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(badge = {
                        if (item.badgeCount != null) {
                            Badge {
                                Text(text= item.badgeCount.toString())
                            }
                        } else if (item.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(imageVector = if(index==selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                            contentDescription = item.title)
                    }

                },
            )
        }
    }
}

@Preview
@Composable
fun PikNavBarPreview() {
    PikNavBar(rememberNavController())
}
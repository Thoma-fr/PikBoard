package com.example.pikboard.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pikboard.R
import com.example.pikboard.api.NetworkResponse
import com.example.pikboard.api.PikBoardApiViewModel
import com.example.pikboard.api.SearchResult
import com.example.pikboard.api.UserApi
import com.example.pikboard.ui.Fragment.FriendTile
import com.example.pikboard.ui.Fragment.PikHeader
import com.example.pikboard.ui.Fragment.PikTextField
import androidx.compose.foundation.shape.CircleShape
import com.example.pikboard.api.CurrentGame
import com.example.pikboard.store.readSessionToken

@Composable
fun RequestButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(40.dp)
            .height(40.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE0E0E0),
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
fun FriendsScreen(viewModel: PikBoardApiViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var friends by remember { mutableStateOf<List<UserApi>>(emptyList()) }
    var pendingRequests by remember { mutableStateOf<List<UserApi>>(emptyList()) }
    var pendingGameRequests by remember { mutableStateOf<List<CurrentGame>>(emptyList()) }
    var searchResults by remember { mutableStateOf<SearchResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf<UserApi?>(null) }

    val context = LocalContext.current
    val tokenValue by readSessionToken(context).collectAsState(initial = "")
    val token = if (tokenValue is String) tokenValue as String else ""
    val friendsResponse by viewModel.friendsResponse.observeAsState()
    val pendingRequestsResponse by viewModel.pendingRequestsResponse.observeAsState()
    val pendingRequestsGameResponse by viewModel.penddingGamesResponse.observeAsState()
    val friendRequestResponse by viewModel.friendRequestResponse.observeAsState()
    val searchUsersResponse by viewModel.searchUsersResponse.observeAsState()
    val userFromSessionTokenResponse by viewModel.userFromSessionTokenResponse.observeAsState()

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.getFriends(token)
            viewModel.getPendingFriendRequests(token)
            viewModel.getUserFromSessionToken(token)
            viewModel.pendingGames(token)
        }
    }

    LaunchedEffect(userFromSessionTokenResponse) {
        when(val result = userFromSessionTokenResponse) {
            is NetworkResponse.Success -> {
                currentUser = result.data.data
            }
            else -> {}
        }
    }

    LaunchedEffect(searchQuery) {
        if (token.isNotEmpty() && searchQuery.isNotEmpty()) {
            viewModel.searchUsers(token, searchQuery)
        } else {
            searchResults = null
        }
    }

    LaunchedEffect(friendsResponse) {
        when(val result = friendsResponse) {
            is NetworkResponse.Success -> {
                friends = result.data.data
                isLoading = false
            }
            is NetworkResponse.Error -> {
                isLoading = false
            }
            NetworkResponse.Loading -> {
                isLoading = true
            }
            null -> {}
        }
    }

    LaunchedEffect(pendingRequestsResponse) {
        when(val result = pendingRequestsResponse) {
            is NetworkResponse.Success -> {
                pendingRequests = result.data.data
                isLoading = false
            }
            is NetworkResponse.Error -> {
                isLoading = false
            }
            NetworkResponse.Loading -> {
                isLoading = true
            }
            null -> {}
        }
    }

    LaunchedEffect(pendingRequestsGameResponse) {
        when(val result = pendingRequestsGameResponse) {
            is NetworkResponse.Error -> {
                isLoading = false
            }
            NetworkResponse.Loading -> {
                isLoading = true
            }
            is NetworkResponse.Success -> {
                pendingGameRequests = result.data.data
                isLoading = false
            }
            null -> {}
        }
    }

    LaunchedEffect(searchUsersResponse) {
        when(val result = searchUsersResponse) {
            is NetworkResponse.Success -> {
                val filteredPotentialFriends = result.data.data.potentialFriends.filter { user ->
                    user.id != currentUser?.id
                }
                searchResults = SearchResult(
                    friends = result.data.data.friends,
                    potentialFriends = filteredPotentialFriends
                )
                isLoading = false
            }
            is NetworkResponse.Error -> {
                isLoading = false
            }
            NetworkResponse.Loading -> {
                isLoading = true
            }
            null -> {}
        }
    }

    LaunchedEffect(friendRequestResponse) {
        when(val result = friendRequestResponse) {
            is NetworkResponse.Success -> {
                if (token.isNotEmpty()) {
                    viewModel.getFriends(token)
                    viewModel.getPendingFriendRequests(token)
                }
            }
            else -> {}
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ){
        PikHeader()

        Spacer(modifier = Modifier.height(16.dp))

        PikTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            hint = "Search...",
            leadingIcon = Icons.Rounded.Search
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            
            if (pendingGameRequests.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "Pending Game Requests",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(pendingGameRequests) { game ->
                        GameRequestTile(
                            name = game.user.username,
                            onAccept = {
                                viewModel.acceptGameRequest(token, game.id, true)
                                pendingGameRequests = pendingGameRequests.filterNot { it.id == game.id }
                            },
                            onReject = {
                                viewModel.acceptGameRequest(token, game.id, false)
                                pendingGameRequests = pendingGameRequests.filterNot { it.id == game.id }
                            }
                        )
                    }
                }
            }

            FriendsList(
                friends = friends,
                pendingRequests = pendingRequests,
                searchResults = searchResults,
                searchQuery = searchQuery,
                onAcceptRequest = { friendId ->
                    if (token.isNotEmpty()) {
                        viewModel.acceptFriendRequest(token, friendId, true)
                    }
                },
                onRejectRequest = { friendId ->
                    if (token.isNotEmpty()) {
                        viewModel.acceptFriendRequest(token, friendId, false)
                    }
                },
                onSendFriendRequest = { userId ->
                    if (token.isNotEmpty()) {
                        viewModel.sendFriendRequest(token, userId)
                    }
                }
            )
        }
    }
}

@Composable
fun FriendsList(
    friends: List<UserApi>,
    pendingRequests: List<UserApi>,
    searchResults: SearchResult?,
    searchQuery: String,
    onAcceptRequest: (Int) -> Unit,
    onRejectRequest: (Int) -> Unit,
    onSendFriendRequest: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val filteredPendingRequests = if (searchQuery.isNotEmpty()) {
            pendingRequests.filter { it.username.contains(searchQuery, ignoreCase = true) }
        } else {
            pendingRequests
        }

        if (filteredPendingRequests.isNotEmpty()) {
            item {
                Text(
                    text = "Pending Friend Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(filteredPendingRequests) { user ->
                FriendRequestTile(
                    name = user.username,
                    onAccept = { onAcceptRequest(user.id) },
                    onReject = { onRejectRequest(user.id) }
                )
            }
        }
        
        if (searchQuery.isNotEmpty()) {
            if (searchResults?.friends?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Friends",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(searchResults.friends) { user ->
                    FriendTile(name = user.username)
                }
            }

            if (searchResults?.potentialFriends?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Users",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(searchResults.potentialFriends) { user ->
                    val isNotInPendingRequests = !pendingRequests.any { it.id == user.id }
                    if (isNotInPendingRequests) {
                        SearchResultTile(
                            name = user.username,
                            onSendRequest = { onSendFriendRequest(user.id) }
                        )
                    }
                }
            } else if (searchResults?.friends?.isEmpty() == true && filteredPendingRequests.isEmpty()) {
                item {
                    Text(
                        text = "No users found",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            }
        } else {
            if (friends.isNotEmpty()) {
                item {
                    Text(
                        text = "Friends",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(friends) { user ->
                    FriendTile(name = user.username)
                }
            }
        }
    }
}

@Composable
fun FriendRequestTile(
    name: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 16.sp, color = Color.Black)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RequestButton(text = "✓", onClick = onAccept)
            RequestButton(text = "✕", onClick = onReject)
        }
    }
}

@Composable
fun GameRequestTile(
    name: String,
    onAccept: () -> Unit,
    onReject: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 16.sp, color = Color.Black)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RequestButton(text = "✓", onClick = onAccept)
            RequestButton(text = "✕", onClick = onReject)
        }
    }
}

@Composable
fun SearchResultTile(
    name: String,
    onSendRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 16.sp, color = Color.Black)
        }
        RequestButton(text = "+", onClick = onSendRequest)
    }
}

@Preview(showBackground = true)
@Composable
fun FriendScreenPreview() {
    FriendsScreen()
}
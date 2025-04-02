package com.example.pikboard.api

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Multipart

class PikBoardApiViewModel: ViewModel(){

    private val pikBoardApi = RetrofitInstance.pikBoardApi
    val loginResponse = MutableLiveData<NetworkResponse<LoginResponse>>()
    val signupResponse = MutableLiveData<NetworkResponse<Unit>>()
    val userFromSessionTokenResponse = MutableLiveData<NetworkResponse<UserResponse>>()
    val fenToImageResponse = MutableLiveData<NetworkResponse<FenToImageResponse>>()
    val friendsResponse = MutableLiveData<NetworkResponse<FriendsResponse>>()
    val pendingRequestsResponse = MutableLiveData<NetworkResponse<FriendsResponse>>()
    val sentRequestsResponse = MutableLiveData<NetworkResponse<FriendsResponse>>()
    val friendRequestResponse = MutableLiveData<NetworkResponse<Unit>>()
    val searchUsersResponse = MutableLiveData<NetworkResponse<SearchResponse>>()
    val imageToFenResponse = MutableLiveData<NetworkResponse<FemResponse>>()
    val currentGamesResponse = MutableLiveData<NetworkResponse<CurrentGameResponse>>()

    fun login(email:String, password: String) {
        loginResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = pikBoardApi.login(LoginRequest(email, password))
                if (response.isSuccessful){
                    response.body()?.let {
                        loginResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    loginResponse.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                Log.i("Error api", e.toString())
                loginResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun signup(username: String, email: String, password: String) {
        signupResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = pikBoardApi.signup(SignupRequest(username, email, password))
                if (response.isSuccessful) {
                    signupResponse.value = NetworkResponse.Success(Unit)
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Username or Email are not valid"
                        409 -> "User already exist"
                        else -> "Server error"
                    }
                    signupResponse.value = NetworkResponse.Error(errorMessage)                }
            } catch (e: Exception) {
                Log.i("Error api", e.toString())
                signupResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun getUserFromSessionToken(token: String) {
        userFromSessionTokenResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.getUserFromSessionToken(bearerToken)
                if (response.isSuccessful){
                    response.body()?.let {
                        userFromSessionTokenResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        else -> "Server error"
                    }
                    userFromSessionTokenResponse.value = NetworkResponse.Error(errorMessage)                }
            } catch (e: Exception) {
                userFromSessionTokenResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun getFenToImage(fen: String, pov: String? = null) {
        // Indique que le chargement commence
        fenToImageResponse.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = pikBoardApi.fenToImage(fen, pov)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        fenToImageResponse.value = NetworkResponse.Success(data)
                    } ?: run {
                        fenToImageResponse.value = NetworkResponse.Error("Pas de données")
                    }
                } else {
                    fenToImageResponse.value = NetworkResponse.Error("Erreur du serveur")
                }
            } catch (e: Exception) {
                fenToImageResponse.value = NetworkResponse.Error("Exception : ${e.message}")
            }
        }
    }
    fun getFriends(token: String) {
        friendsResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.getFriends(bearerToken)
                if (response.isSuccessful) {
                    response.body()?.let {
                        friendsResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    friendsResponse.value = NetworkResponse.Error("Failed to load friends")
                }
            } catch (e: Exception) {
                friendsResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun getPendingFriendRequests(token: String) {
        pendingRequestsResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.getPendingFriendRequests(bearerToken)
                if (response.isSuccessful) {
                    response.body()?.let {
                        pendingRequestsResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    pendingRequestsResponse.value = NetworkResponse.Error("Failed to load pending requests")
                }
            } catch (e: Exception) {
                pendingRequestsResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun getSentFriendRequests(token: String) {
        sentRequestsResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.getSentFriendRequests(bearerToken)
                if (response.isSuccessful) {
                    response.body()?.let {
                        sentRequestsResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    sentRequestsResponse.value = NetworkResponse.Error("Failed to load sent requests")
                }
            } catch (e: Exception) {
                sentRequestsResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun sendFriendRequest(token: String, userId: Int) {
        friendRequestResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.sendFriendRequest(bearerToken, userId)
                if (response.isSuccessful) {
                    friendRequestResponse.value = NetworkResponse.Success(Unit)
                } else {
                    friendRequestResponse.value = NetworkResponse.Error("Failed to send friend request")
                }
            } catch (e: Exception) {
                friendRequestResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun acceptFriendRequest(token: String, friendId: Int, accept: Boolean) {
        friendRequestResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.acceptFriendRequest(bearerToken, friendId, mapOf("answer" to accept))
                if (response.isSuccessful) {
                    friendRequestResponse.value = NetworkResponse.Success(Unit)
                } else {
                    friendRequestResponse.value = NetworkResponse.Error("Failed to ${if (accept) "accept" else "reject"} friend request")
                }
            } catch (e: Exception) {
                friendRequestResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun searchUsers(token: String, query: String) {
        searchUsersResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val friendsResponse = pikBoardApi.getFriends(bearerToken)
                val friends = friendsResponse.body()?.data ?: emptyList()
                val matchingFriends = friends.filter { it.username.contains(query, ignoreCase = true) }
                val response = pikBoardApi.searchUsers(bearerToken, query)
                if (response.isSuccessful) {
                    response.body()?.let { searchResult ->
                        val currentUser = userFromSessionTokenResponse.value?.let { 
                            (it as? NetworkResponse.Success)?.data?.data 
                        }
                        val filteredUsers = searchResult.data.filter { user ->
                            user.id != currentUser?.id && !friends.any { it.id == user.id }
                        }
                        searchUsersResponse.value = NetworkResponse.Success(SearchResponse(
                            SearchResult(
                                friends = matchingFriends,
                                potentialFriends = filteredUsers
                            )
                        ))
                    } ?: run {
                        searchUsersResponse.value = NetworkResponse.Error("Empty response")
                    }
                } else {
                    searchUsersResponse.value = NetworkResponse.Error("Failed to search users: ${response.code()}")
                }
            } catch (e: Exception) {
                searchUsersResponse.value = NetworkResponse.Error("Read crash: ${e.message}")
            }
        }
    }

    fun imageToFen(token: String, img: MultipartBody.Part) {
        imageToFenResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.imageToFen(bearerToken, img)
                if (response.isSuccessful) {
                    response.body()?.let {
                        imageToFenResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    imageToFenResponse.value = NetworkResponse.Error("Server error")
                }
            } catch (e: Exception) {
                imageToFenResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }

    fun currentGame(token: String) {
        currentGamesResponse.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = pikBoardApi.currentGames(bearerToken)
                if (response.isSuccessful) {
                    response.body()?.let {
                        currentGamesResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    currentGamesResponse.value = NetworkResponse.Error("Server error")
                }
            } catch (e: Exception) {
                currentGamesResponse.value = NetworkResponse.Error("Read crash")
            }
        }
    }
}

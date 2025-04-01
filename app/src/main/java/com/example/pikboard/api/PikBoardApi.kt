package com.example.pikboard.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


data class Token(
    val token: String
)

data class LoginResponse(
    val `data`: Token
)

data class UserApi(
    val id: Int,
    val image: String,
    val username: String,
    val email: String,
    val phone: String,
    val created_at: String,
    val updated_at: String
)

data class UserResponse(
    var `data`: UserApi
)

data class FriendsResponse(
    val `data`: List<UserApi>
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String
)

data class SearchResult(
    val friends: List<UserApi>,
    val potentialFriends: List<UserApi>
)

data class SearchResponse(
    val `data`: SearchResult
)

interface PikBoardApi {
    @POST("login")
    suspend fun login(
        @Body() request: LoginRequest
    ): Response<LoginResponse>

    @POST("signup")
    suspend fun signup(
        @Body() request:SignupRequest
    ):Response<Void>

    @GET("user/self")
    suspend fun getUserFromSessionToken(
        @Header("Authorization") token: String
    ):Response<UserResponse>

    @GET("user/friends")
    suspend fun getFriends(
        @Header("Authorization") token: String
    ): Response<FriendsResponse>

    @GET("friend/request")
    suspend fun getPendingFriendRequests(
        @Header("Authorization") token: String
    ): Response<FriendsResponse>

    @GET("friend/sent")
    suspend fun getSentFriendRequests(
        @Header("Authorization") token: String
    ): Response<FriendsResponse>

    @POST("friend/request")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Query("id") userId: Int
    ): Response<Unit>

    @POST("friend/accept")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Query("friend_id") friendId: Int,
        @Body answer: Map<String, Boolean>
    ): Response<Unit>

    @GET("user/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("username") query: String
    ): Response<FriendsResponse>
}

package com.example.pikboard.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


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

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String
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
}

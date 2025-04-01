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
data class FenToImageResponse(
    val data: String
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

    @GET("Chess/chess")
    suspend fun fenToImage(
        @Query("q") fen: String,
        @Query("pov") pov: String? = null  // facultatif, ici par exemple "black" si besoin
    ): Response<FenToImageResponse>


}

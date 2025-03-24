package com.example.pikboard.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class Data(
    val token: String
)

data class LoginResponse(
    val `data`: Data
)

data class LoginRequest(
    val email: String,
    val password: String
)
interface PikBoardApi {
    @POST("login")
    suspend fun login(
        @Body() request: LoginRequest
    ): Response<LoginResponse>
}

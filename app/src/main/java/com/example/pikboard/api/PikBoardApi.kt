package com.example.pikboard.api

import android.graphics.Bitmap
import androidx.compose.ui.Modifier
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.ByteArrayOutputStream



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

data class PendingGamesResponse(
    val `data`: List<CurrentGame>
)

data class FemResponse(
    val `data`: String
)

data class CurrentGameResponse(
    val `data`: List<CurrentGame>
)

data class CurrentGame(
    val id: Int,
    val user: UserApi,
    val opponent: UserApi,
    val board: String,
    // TODO: Ajouter les status
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class CreateGameRequest (
    val fen: String,
    val opponent_id: Int,
)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String
)

data class FenToImageResponse(
    val data: String
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

    @GET("Chess/chess")
    suspend fun fenToImage(
        @Query("q") fen: String,
        @Query("pov") pov: String? = null 
    ): Response<FenToImageResponse>

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

    @POST("game/accept")
    suspend fun acceptGameRequest(
        @Header("Authorization") token: String,
        @Query("g") gameID: Int,
        @Body answer: Map<String, Boolean>
    ): Response<Unit>

    @GET("user/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("username") query: String
    ): Response<FriendsResponse>

    fun Bitmap.toMultipartBodyPart(partName: String): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody: RequestBody = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, "image.png", requestBody)
    }

    @Multipart
    @POST("game/position")
    suspend fun imageToFen(
        @Header("Authorization") token: String,
        @Part img: MultipartBody.Part
    ): Response<FemResponse>

    @GET("game/current")
    suspend fun currentGames(
        @Header("Authorization") token: String,
    ): Response<CurrentGameResponse>

    @GET("game/request")
    suspend fun pendingGames(
        @Header("Authorization") token: String
    ): Response<PendingGamesResponse>


    @POST("game/new")
    suspend fun createGame(
        @Header("Authorization") token: String,
        @Body() request: CreateGameRequest
    ): Response<Unit>
}

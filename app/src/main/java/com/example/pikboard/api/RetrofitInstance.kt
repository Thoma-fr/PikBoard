package com.example.pikboard.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseURL = "https://gnat-happy-drake.ngrok-free.app/v1/";

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pikBoardApi: PikBoardApi = getInstance().create(PikBoardApi::class.java)
}
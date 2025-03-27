package com.example.pikboard.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PikBoardApiViewModel: ViewModel(){

    private val pikBoardApi = RetrofitInstance.pikBoardApi
    val loginResponse = MutableLiveData<NetworkResponse<LoginResponse>>()
    val signupResponse = MutableLiveData<NetworkResponse<Unit>>()
    val userFromSessionTokenResponse = MutableLiveData<NetworkResponse<UserResponse>>()

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
}
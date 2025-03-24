package com.example.pikboard.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PikBoardApiViewModel: ViewModel(){

    private val pikBoardApi = RetrofitInstance.pikBoardApi
    val loginResponse = MutableLiveData<NetworkResponse<LoginResponse>>()

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

}
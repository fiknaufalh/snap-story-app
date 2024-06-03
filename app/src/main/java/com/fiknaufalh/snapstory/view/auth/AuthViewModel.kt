package com.fiknaufalh.snapstory.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiknaufalh.snapstory.data.UserRepository
import com.fiknaufalh.snapstory.data.pref.UserModel
import com.fiknaufalh.snapstory.data.remote.responses.ErrorResponse
import com.fiknaufalh.snapstory.data.remote.responses.LoginResponse
import com.fiknaufalh.snapstory.data.remote.responses.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession() = repository.getSession()

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val responseLiveData = MutableLiveData<RegisterResponse>()
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                responseLiveData.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                responseLiveData.postValue(errorResponse)
            }
        }
        return responseLiveData
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        val responseLiveData = MutableLiveData<Result<LoginResponse>>()
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                responseLiveData.postValue(Result.success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                responseLiveData.postValue(Result.failure(Exception(errorMessage)))
            }
        }
        return responseLiveData
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
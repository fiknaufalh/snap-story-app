package com.fiknaufalh.snapstory.data

import com.fiknaufalh.snapstory.data.pref.UserModel
import com.fiknaufalh.snapstory.data.pref.UserPreference
import com.fiknaufalh.snapstory.data.remote.responses.LoginResponse
import com.fiknaufalh.snapstory.data.remote.responses.RegisterResponse
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class MainRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun fetchStories() = apiService.getStories()

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
package com.fiknaufalh.snapstory.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fiknaufalh.snapstory.data.StoryPagingSource
import com.fiknaufalh.snapstory.data.pref.UserModel
import com.fiknaufalh.snapstory.data.pref.UserPreference
import com.fiknaufalh.snapstory.data.remote.responses.FileUploadResponse
import com.fiknaufalh.snapstory.data.remote.responses.LoginResponse
import com.fiknaufalh.snapstory.data.remote.responses.RegisterResponse
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
): Repository {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun fetchStories() = apiService.getStories()

    fun fetchStoriesPaging(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

//    fun getStoryPagingSource(): StoryPagingSource {
//        Log.d("StoryRepository", "getStoryPagingSource called")
//        return StoryPagingSource(apiService)
//    }

    fun fetchStoriesWithLocation() = apiService.getStoriesWithLocation()

    suspend fun uploadImage(multipartBody: MultipartBody.Part, description: RequestBody):
            FileUploadResponse {
        return apiService.uploadImage(multipartBody, description)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
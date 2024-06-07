package com.fiknaufalh.snapstory.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.fiknaufalh.snapstory.BuildConfig
import com.fiknaufalh.snapstory.repositories.UserRepository
import com.fiknaufalh.snapstory.data.pref.UserModel
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.data.remote.responses.StoryResponse
import com.fiknaufalh.snapstory.repositories.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
//    val stories: LiveData<StoryResponse> = _stories
    val stories: LiveData<PagingData<StoryItem>> = repository
        .fetchStoriesPaging().cachedIn(viewModelScope)
//    val stories: LiveData<PagingData<StoryItem>> = Pager(
//        config = PagingConfig(
//            pageSize = 10,
//            enablePlaceholders = false
//        ),
//        pagingSourceFactory = { repository.getStoryPagingSource() }
//    ).liveData.cachedIn(viewModelScope).also {
//        Log.d("MainViewModel", "Pager created")
//    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<Boolean?>()
    val errorToast: LiveData<Boolean?> = _errorToast

//    fun fetchStories() {
//        _isLoading.value = true
//        val client = repository.fetchStories()
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>,
//                response: Response<StoryResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _stories.value = response.body()
//                    _errorToast.value = true
//                }
//                else {
//                    if (BuildConfig.DEBUG) Log.d(TAG, "onFailResponse: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
//                _isLoading.value = false
//                _errorToast.value = false
//            }
//        })
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun resetToast() {
        _errorToast.value = null
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
package com.fiknaufalh.snapstory.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fiknaufalh.snapstory.BuildConfig
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.data.remote.responses.StoryResponse
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStoriesPaging(page, params.loadSize)
            val listData = response.listStory
            Log.d(TAG, "Data loaded from API: $listData")
            LoadResult.Page(
                data = listData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (listData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e(TAG, "Error loading data: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

//    private fun getStoriesPaging(page: Int, size: Int): List<StoryItem> {
//        val client = apiService.getStoriesPaging(page, size)
//        val stories = MutableLiveData<List<StoryItem>>()
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>,
//                response: Response<StoryResponse>
//            ) {
//                if (response.isSuccessful) {
//                    stories.value = response.body()?.listStory
//                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse(stories.value): ${stories.value}")
//                }
//                else {
//                    if (BuildConfig.DEBUG) Log.d(TAG, "onFailResponse: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
//            }
//        })
//        return stories.value ?: emptyList()
//    }

    private companion object {
        const val TAG = "StoryPagingSource"
        const val INITIAL_PAGE_INDEX = 1
    }
}
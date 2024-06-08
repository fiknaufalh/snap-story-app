package com.fiknaufalh.snapstory.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fiknaufalh.snapstory.BuildConfig
import com.fiknaufalh.snapstory.data.remote.responses.StoryItem
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiService

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
            if (BuildConfig.DEBUG) Log.d(TAG, "Data loaded from API: $listData")
            LoadResult.Page(
                data = listData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (listData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Error loading data: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val TAG = "StoryPagingSource"
        const val INITIAL_PAGE_INDEX = 1
    }
}
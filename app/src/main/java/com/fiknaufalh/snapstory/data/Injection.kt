package com.fiknaufalh.snapstory.data

import android.content.Context
import com.fiknaufalh.snapstory.data.pref.UserPreference
import com.fiknaufalh.snapstory.data.pref.dataStore
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiConfig
import com.fiknaufalh.snapstory.view.auth.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}
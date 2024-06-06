package com.fiknaufalh.snapstory.di

import android.content.Context
import com.fiknaufalh.snapstory.data.pref.UserPreference
import com.fiknaufalh.snapstory.data.pref.dataStore
import com.fiknaufalh.snapstory.data.remote.retrofit.ApiConfig
import com.fiknaufalh.snapstory.data.MainRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return MainRepository.getInstance(pref, apiService)
    }
}
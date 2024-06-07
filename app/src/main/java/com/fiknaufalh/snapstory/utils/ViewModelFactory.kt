package com.fiknaufalh.snapstory.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiknaufalh.snapstory.di.Injection
import com.fiknaufalh.snapstory.repositories.Repository
import com.fiknaufalh.snapstory.repositories.StoryRepository
import com.fiknaufalh.snapstory.repositories.UserRepository
import com.fiknaufalh.snapstory.view.auth.AuthViewModel
import com.fiknaufalh.snapstory.view.main.MainViewModel
import com.fiknaufalh.snapstory.view.maps.MapsViewModel
import com.fiknaufalh.snapstory.view.upload.UploadViewModel
import com.fiknaufalh.snapstory.view.welcome.WelcomeViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository as StoryRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as UserRepository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository as StoryRepository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository as UserRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository as StoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, type: String): ViewModelFactory {
            synchronized(ViewModelFactory::class.java) {
                when (type) {
                    "user" -> INSTANCE = ViewModelFactory(Injection.provideUserRepository(context))
                    "story" -> INSTANCE = ViewModelFactory(Injection.provideStoryRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
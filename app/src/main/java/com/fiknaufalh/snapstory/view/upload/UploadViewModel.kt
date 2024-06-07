package com.fiknaufalh.snapstory.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiknaufalh.snapstory.repositories.UserRepository
import com.fiknaufalh.snapstory.data.remote.responses.FileUploadResponse
import com.fiknaufalh.snapstory.repositories.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UploadViewModel(private val repository: StoryRepository) : ViewModel() {

    fun uploadImage(imageFile: File, description: String): LiveData<FileUploadResponse> {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val responseLiveData = MutableLiveData<FileUploadResponse>()
        viewModelScope.launch {
            try {
                val successResponse = repository.uploadImage(multipartBody, requestBody)
                responseLiveData.postValue(successResponse)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                responseLiveData.postValue(errorResponse)
            }
        }
        return responseLiveData
    }


    companion object {
        private const val TAG = "StoryViewModel"
    }
}
package com.fiknaufalh.snapstory.data.remote.retrofit

import com.fiknaufalh.snapstory.data.remote.responses.FileUploadResponse
import com.fiknaufalh.snapstory.data.remote.responses.LoginResponse
import com.fiknaufalh.snapstory.data.remote.responses.RegisterResponse
import com.fiknaufalh.snapstory.data.remote.responses.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    fun getStories(): Call<StoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse

}
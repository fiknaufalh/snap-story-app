package com.fiknaufalh.snapstory.data.remote.retrofit

import com.fiknaufalh.snapstory.data.remote.responses.LoginResponse
import com.fiknaufalh.snapstory.data.remote.responses.RegisterResponse
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

}
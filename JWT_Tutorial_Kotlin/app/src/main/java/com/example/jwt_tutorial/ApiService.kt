package com.example.jwt_tutorial

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/auth/register")
    fun signUp(@Body signUp: SignUp): Call<RegisterResponse>

    @POST("/auth/login")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun login(@Field("username") username : String, @Field("password") password : String): Call<LoginResponse>

    @POST("/auth/refresh")
    fun refresh(@Header("Authorization") refreshToken: String): Call<RefreshResponse>

    @GET("/auth/protected")
    fun checkService(): Call<CheckResponse>

}
package com.example.jwt_tutorial

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("email") val email: String,
    @SerializedName("user_id") val user_id: Int
    )

data class LoginResponse (
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("token_type") val token_type: String
    )

data class RefreshResponse (
    @SerializedName("access_token") val access_token: String
    )

data class CheckResponse (
    @SerializedName("detail") val detail: String
    )

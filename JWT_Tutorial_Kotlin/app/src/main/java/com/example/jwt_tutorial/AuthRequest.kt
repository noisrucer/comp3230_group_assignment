package com.example.jwt_tutorial

import com.google.gson.annotations.SerializedName

data class SignUp(
    @SerializedName(value = "email") val email : String,
    @SerializedName(value = "password") val password : String
)



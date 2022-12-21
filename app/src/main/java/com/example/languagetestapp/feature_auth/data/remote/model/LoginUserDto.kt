package com.example.languagetestapp.feature_auth.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginUserDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null
)

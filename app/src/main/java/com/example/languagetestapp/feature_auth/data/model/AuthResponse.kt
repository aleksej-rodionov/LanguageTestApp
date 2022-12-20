package com.example.languagetestapp.feature_auth.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)

package com.example.languagetestapp.feature_auth.data.remote.model

import com.google.gson.annotations.SerializedName

data class AuthResponse<T>( // todo create 1 universal class
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)

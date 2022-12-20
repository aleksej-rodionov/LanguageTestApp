package com.example.languagetestapp.feature_auth.data.model

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

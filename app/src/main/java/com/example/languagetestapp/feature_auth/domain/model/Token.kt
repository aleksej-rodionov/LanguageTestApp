package com.example.languagetestapp.feature_auth.domain.model

data class Token(
    val accessToken: String,
    val refreshToken: String
)
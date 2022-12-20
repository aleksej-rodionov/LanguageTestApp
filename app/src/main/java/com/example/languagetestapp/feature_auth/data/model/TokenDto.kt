package com.example.languagetestapp.feature_auth.data.model

import com.example.languagetestapp.feature_auth.domain.model.Token
import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
) {

    fun toToken(): Token {
        return Token(accessToken, refreshToken)
    }
}

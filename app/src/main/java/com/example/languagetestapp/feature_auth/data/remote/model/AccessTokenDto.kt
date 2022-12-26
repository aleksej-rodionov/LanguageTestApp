package com.example.languagetestapp.feature_auth.data.remote.model

import com.example.languagetestapp.feature_auth.domain.model.Token
import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accessTokenExp")
    val accessTokenExp: Long
) {

    fun toToken(): Token {
        return Token(accessToken, accessTokenExp, null)
    }
}

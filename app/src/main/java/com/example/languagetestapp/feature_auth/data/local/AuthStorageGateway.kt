package com.example.languagetestapp.feature_auth.data.local

interface AuthStorageGateway {

    fun storeAccessToken(accessToken: String)
    fun fetchAccessToken(): String?
    fun clearAccessToken()

    fun storeAccessTokenExp(exp: Long)
    fun fetchAccessTokenExp(): Long?
    fun clearAccessTokenExp()

    fun storeRefreshToken(refreshToken: String?)
    fun fetchRefreshToken(): String?
    fun clearRefreshToken()
}
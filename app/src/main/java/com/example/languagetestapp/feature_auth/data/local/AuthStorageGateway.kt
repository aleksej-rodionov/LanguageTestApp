package com.example.languagetestapp.feature_auth.data.local

import com.example.languagetestapp.feature_auth.domain.model.User

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

    fun storeCurrentUserData(user: User)
    fun fetchCurrentUserData(): User?
    fun clearCurrentUserData()
}
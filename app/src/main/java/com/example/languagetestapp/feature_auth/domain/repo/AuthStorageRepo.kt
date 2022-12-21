package com.example.languagetestapp.feature_auth.domain.repo

import com.example.languagetestapp.feature_auth.data.local.model.UserEntity

interface AuthStorageRepo {

    suspend fun storeAccessToken(accessToken: String)

    suspend fun fetchAccessToken(): Result<String>

    suspend fun storeRefreshToken(refreshToken: String)

    suspend fun fetchRefreshToken(): Result<String>

    suspend fun storeCurrentUser(user: UserEntity)

    suspend fun fetchCurrentUser(): Result<UserEntity>
}
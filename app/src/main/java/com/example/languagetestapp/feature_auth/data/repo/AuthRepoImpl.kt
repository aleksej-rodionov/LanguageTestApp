package com.example.languagetestapp.feature_auth.data.repo

import android.util.Log
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.model.LoginUserDto
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_auth.util.Constants.TAG_AUTH
import com.example.languagetestapp.feature_auth.util.countExp

class AuthRepoImpl(
    private val authApi: LanguageAuthApi,
    private val authStorageGateway: AuthStorageGateway
): AuthRepo {

    override suspend fun login(email: String, password: String): Resource<Token> {
        val response = authApi.login(LoginUserDto(email, password))

        Log.d(TAG_AUTH, "login: $response")

        if (response.status == "ok") {

            response.body?.let {

                val accessToken = it.accessToken
                authStorageGateway.storeAccessToken(accessToken)
                val accessTokenExp = it.countExp()
                authStorageGateway.storeAccessTokenExp(accessTokenExp)
                val refreshToken = it.refreshToken
                authStorageGateway.storeRefreshToken(refreshToken)

                return Resource.Success(response.body.toToken())
            } ?: run {
                return Resource.Error("Response is successful, but token not found")
            }

        } else {
            return Resource.Error(response.error ?: "Unknown error occurred")
        }
    }

    override suspend fun refreshToken(): Resource<Token> {
        val refreshTokenStored = authStorageGateway.fetchRefreshToken()
        refreshTokenStored?.let {
            val response = authApi.refresh(it)

            Log.d(TAG_AUTH, "refreshToken: $response")

            if (response.status == "ok") {

                response.body?.let {

                    val accessToken = it.accessToken
                    authStorageGateway.storeAccessToken(accessToken)
                    val accessTokenExp = it.countExp()
                    authStorageGateway.storeAccessTokenExp(accessTokenExp)
                    val refreshToken = it.refreshToken
                    authStorageGateway.storeRefreshToken(refreshToken)

                    return Resource.Success(response.body.toToken())
                } ?: run {
                    return Resource.Error("Response is successful, but token not found")
                }

            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } ?: return Resource.Error("No refreshToken in pref storage")
    }

    override suspend fun logout(): Resource<String> {

        // clear tokens in authStore
        authStorageGateway.apply {
            clearAccessToken()
            clearAccessTokenExp()
            clearRefreshToken()
        }

        // clear token on the backEnd
        val refreshTokenStored = authStorageGateway.fetchRefreshToken()
        refreshTokenStored?.let {
            val response = authApi.logout(it)

            if (response.status == "ok") {
                return Resource.Success(response.body ?: "Success, anyway")
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } ?: return Resource.Error("No refreshToken in pref storage")
    }

    override fun fetchAccessToken(): String? {
        return authStorageGateway.fetchAccessToken()
    }
}
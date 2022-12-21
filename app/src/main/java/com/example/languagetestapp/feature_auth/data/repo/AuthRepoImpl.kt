package com.example.languagetestapp.feature_auth.data.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.model.LoginUserDto
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class AuthRepoImpl(
    private val authApi: LanguageAuthApi,
//    private val authStorageGateway: AuthStorageGateway
    private val authStorageGateway: AuthStorageGateway
): AuthRepo {

    override suspend fun login(email: String, password: String): Resource<Token> {
        val response = authApi.login(LoginUserDto(email, password))

        if (response.status == "ok") {

            response.body?.let {

                val accessToken = it.accessToken
                authStorageGateway.storeAccessToken(accessToken)
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
}
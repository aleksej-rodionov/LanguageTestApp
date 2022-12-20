package com.example.languagetestapp.feature_auth.data.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.model.LoginUserDto
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class AuthRepoImpl(
    private val authApi: LanguageAuthApi
): AuthRepo {

    override suspend fun login(email: String, password: String): Resource<Token> {
        val response = authApi.login(LoginUserDto(email, password))
        if (response.status == "ok") {
            response.body?.let { dto ->
                return Resource.Success(dto.toToken())
            } ?: return Resource.Error("Token not found")
        } else {
            return Resource.Error(response.error ?: "Unknown error occurred")
        }
    }
}
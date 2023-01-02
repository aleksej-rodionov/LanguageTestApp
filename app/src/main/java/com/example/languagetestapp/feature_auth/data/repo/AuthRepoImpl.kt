package com.example.languagetestapp.feature_auth.data.repo

import android.util.Log
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.model.LoginUserDto
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.data.remote.LanguageUserApi
import com.example.languagetestapp.feature_auth.data.remote.model.TokenDto
import com.example.languagetestapp.feature_auth.data.remote.model.UserDto
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_auth.util.Constants
import com.example.languagetestapp.feature_auth.util.Constants.TAG_AUTH
import com.example.languagetestapp.feature_auth.util.Constants.TAG_USER
import com.example.languagetestapp.feature_auth.util.countExp

class AuthRepoImpl(
    private val authApi: LanguageAuthApi,
    private val userApi: LanguageUserApi,
    private val authStorageGateway: AuthStorageGateway
) : AuthRepo {

    override suspend fun register(email: String, password: String): Resource<User> {
        try {
            val response = authApi.register(email, password)
            if (response.status == "ok") {
                response.body?.let {
                    val user = it.toUser()
                    return Resource.Success(user)
                } ?: run {
                    return Resource.Error("Success, but user data not found")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun login(email: String, password: String): Resource<Token> {
        try {
            val response = authApi.login(email, password)
            if (response.status == "ok") {
                response.body?.let {
                    storeAllTokenData(it)
                    getCurrentUserDataAndStoreInPref()

                    return Resource.Success(response.body.toToken())
                } ?: run {
                    return Resource.Error("Response is successful, but token not found")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Resource<String> {
        try {
            val response = authApi.changePassword(oldPassword, newPassword)
            if (response.status == "ok") {
                response.body?.let {
                    storeNewPassword(it)
                    return Resource.Success(it)
                } ?: run {
                    return Resource.Error("Response is success, but newPassword not found")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun refreshToken(): Resource<Token> {
        try {
            val refreshTokenStored = authStorageGateway.fetchRefreshToken()
            refreshTokenStored?.let {
                val response = authApi.refresh(it)
//            Log.d(TAG_AUTH, "refreshToken: $response")

                if (response.status == "ok") {
                    response.body?.let {
                        storeAllTokenData(it)
                        return Resource.Success(response.body.toToken())
                    } ?: run {
                        return Resource.Error("Response is successful, but token not found")
                    }
                } else {
                    return Resource.Error(response.error ?: "Unknown error occurred")
                }
            } ?: return Resource.Error("No refreshToken in pref storage")
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun logout(): Resource<String> {
        val refreshTokenStored = authStorageGateway.fetchRefreshToken()

        // clear token on the backEnd
        try {
            refreshTokenStored?.let {
                val response = authApi.logout(it)

                // clear tokens in authStore
                authStorageGateway.apply {
                    clearAccessToken()
                    clearAccessTokenExp()
                    clearRefreshToken()
                    clearCurrentUserData()
                }

                if (response.status == "ok") {
                    return Resource.Success(response.body ?: "Success, anyway")
                } else {
                    return Resource.Error(response.error ?: "Unknown error occurred")
                }
            } ?: return Resource.Error("No refreshToken in pref storage")
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override fun fetchAccessToken(): String? {
        return authStorageGateway.fetchAccessToken()
    }

    override suspend fun getCurrentUserInfo(): Resource<User> {

        try {
            val response = userApi.getCurrentUserInfo()
            Log.d(TAG_USER, "getCurrentUserInfo: $response")

            if (response.status == "ok") {
                response.body?.let {
                    val user = it.toUser()
                    return Resource.Success(user)
                } ?: run {
                    Log.d(TAG_USER, "getCurrentUserInfo: " + "Success but body is null")
                    return Resource.Error("Success but body is null")
                }
            } else {
                Log.d(TAG_USER, "getCurrentUserInfo: " + response.error ?: "Unknown error occurred")
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Log.d(TAG_USER, "getCurrentUserInfo: " + e.message ?: "Unknown error occurred")
            return Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    //====================PRIVATE METHODS====================
    private fun storeAllTokenData(token: TokenDto) {
        val accessToken = token.accessToken
        authStorageGateway.storeAccessToken(accessToken)
        val accessTokenExp = token.countExp()
        authStorageGateway.storeAccessTokenExp(accessTokenExp)
        val refreshToken = token.refreshToken
        authStorageGateway.storeRefreshToken(refreshToken)
    }

    private fun storeNewPassword(newPassword: String) {
        val currentUserData = authStorageGateway.fetchCurrentUserData()
        currentUserData?.let {
            val updatedUser = it.copy(password = newPassword)
            authStorageGateway.storeCurrentUserData(updatedUser)
        }
    }

    private suspend fun getCurrentUserDataAndStoreInPref() {
        val userResponse = getCurrentUserInfo()
        Log.d(TAG_USER, "login: user = ${userResponse ?: "NULL"}")
        when (userResponse) {
            is Resource.Success -> {
                val user = userResponse.data
                user?.let {
                    authStorageGateway.storeCurrentUserData(it)
                }
            }
            is Resource.Error -> {
                Log.d(
                    TAG_USER, "getCurrentUserDataAndStoreInPref: ${
                        userResponse.message ?: "Unknown error"
                    }"
                )
            }
        }
    }
}
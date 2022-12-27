package com.example.languagetestapp.feature_auth.domain.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.remote.model.UserDto
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.model.User

interface AuthRepo {

    suspend fun register(email: String, password: String): Resource<User>

    suspend fun login(email: String, password: String): Resource<Token>

    suspend fun refreshToken(): Resource<Token>

    suspend fun logout(): Resource<String>

    fun fetchAccessToken(): String?
}
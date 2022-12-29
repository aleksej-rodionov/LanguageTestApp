package com.example.languagetestapp.feature_auth.data.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.LanguageUserApi
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_auth.domain.repo.UserRepo

class UserRepoImpl(
    private val userApi: LanguageUserApi,
    private val authStorageGateway: AuthStorageGateway
): UserRepo {

    override suspend fun getCurrentUserInfo(): Resource<User> {
        TODO("Not yet implemented")
    }
}
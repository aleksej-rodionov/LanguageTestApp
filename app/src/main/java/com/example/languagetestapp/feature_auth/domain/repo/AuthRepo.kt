package com.example.languagetestapp.feature_auth.domain.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.remote.UserResponse
import com.example.languagetestapp.feature_auth.data.remote.model.UserDto
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.model.User

//todo move all UserApi methods to new UserRepo
interface AuthRepo {

    suspend fun register(email: String, password: String): Resource<User>

    suspend fun login(email: String, password: String): Resource<Token>

    //todo move all UserApi methods to new UserRepo
    suspend fun changePassword(oldPassword: String, newPassword: String): Resource<String>

    suspend fun refreshToken(): Resource<Token>

    suspend fun logout(): Resource<String>

    fun fetchAccessToken(): String?

    //todo move all UserApi methods to new UserRepo
    suspend fun getCurrentUserInfo(): Resource<User>

    suspend fun updateAva(avaUrl: String): Resource<String>

    //todo move all UserApi methods to new UserRepo
    suspend fun updateAvaInLocalData(avaUrl: String): Resource<User>

    //todo move all UserApi methods to new UserRepo
    fun fetchLocalUserData(): User?
}
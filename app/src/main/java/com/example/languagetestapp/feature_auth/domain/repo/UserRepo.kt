package com.example.languagetestapp.feature_auth.domain.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.User

interface UserRepo {

    suspend fun getCurrentUserInfo(): Resource<User>
}
package com.example.languagetestapp.feature_auth.domain.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.Token

interface AuthRepo {

    suspend fun login(email: String, password: String): Resource<Token>
}
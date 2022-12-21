package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.Token
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class Login(
    private val authRepo: AuthRepo
) {

    suspend fun execute(email: String, password: String): Resource<Token> {
        return authRepo.login(email, password)
    }
}
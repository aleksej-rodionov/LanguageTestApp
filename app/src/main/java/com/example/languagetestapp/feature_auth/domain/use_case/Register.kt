package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class Register(
    private val authRepo: AuthRepo
) {

    suspend fun execute(email: String, password: String): Resource<User> {
        return authRepo.register(email, password)
    }
}
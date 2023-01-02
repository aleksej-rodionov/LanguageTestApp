package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class ChangePassword(
    private val authRepo: AuthRepo
) {

    suspend fun execute(oldPassword: String, newPassword: String): Resource<String> {
        return authRepo.changePassword(oldPassword, newPassword)
    }
}
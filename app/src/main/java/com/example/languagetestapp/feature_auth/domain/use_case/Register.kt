package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class Register(
    private val authRepo: AuthRepo
) {

    fun execute(email: String, password: String): User {
        // todo
        return User("huy", "pizda", "dzhigurda")
    }
}
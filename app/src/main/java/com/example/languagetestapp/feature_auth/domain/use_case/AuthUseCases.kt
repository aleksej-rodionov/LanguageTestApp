package com.example.languagetestapp.feature_auth.domain.use_case

data class AuthUseCases(
    val register: Register,
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword,
    val validateRepeatedPassword: ValidateRepeatedPassword,
    val validateTerms: ValidateTerms
)

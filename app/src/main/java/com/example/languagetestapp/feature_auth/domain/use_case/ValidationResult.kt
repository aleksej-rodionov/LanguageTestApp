package com.example.languagetestapp.feature_auth.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)

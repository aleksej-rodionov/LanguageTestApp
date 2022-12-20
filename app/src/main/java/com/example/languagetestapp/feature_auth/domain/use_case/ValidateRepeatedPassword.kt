package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.feature_auth.domain.use_case.ValidationResult

class ValidateRepeatedPassword {

    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                false,
                "The passwords don't match"
            )
        }

        return ValidationResult(true)
    }
}
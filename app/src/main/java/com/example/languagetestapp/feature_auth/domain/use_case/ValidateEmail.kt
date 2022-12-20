package com.example.languagetestapp.feature_auth.domain.use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                false,
                "The email can't be empty"
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                false,
                "Not a valid email"
            )
        }

        return ValidationResult(true)
    }
}
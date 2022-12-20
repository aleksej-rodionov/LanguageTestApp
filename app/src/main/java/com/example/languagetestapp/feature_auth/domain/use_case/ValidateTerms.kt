package com.example.languagetestapp.feature_auth.domain.use_case

import com.example.languagetestapp.feature_auth.domain.use_case.ValidationResult

class ValidateTerms {

    fun execute(termsAccepted: Boolean): ValidationResult {
        if (!termsAccepted) {
            return ValidationResult(
                false,
                "Please accept the terms"
            )
        }

        return ValidationResult(true)
    }
}
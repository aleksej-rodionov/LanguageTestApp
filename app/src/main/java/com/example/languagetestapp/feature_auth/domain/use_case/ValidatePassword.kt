package com.example.languagetestapp.feature_auth.domain.use_case

class ValidatePassword {

    fun execute(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                false,
                "The password can't be empty"
            )
        }

        if (password.length < 6) {
            return ValidationResult(
                false,
                "The password should contain at least 6 symbols"
            )
        }

        val containsLettersAndDigits = password.any {
            it.isDigit()
        } && password.any {
            it.isLetter()
        }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                false,
                "The password should contain at least 1 letter and 1 digit"
            )
        }

        return ValidationResult(true)
    }
}
package com.example.languagetestapp.feature_auth.domain.use_case

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `Password is letter-only, returns error`() {
        val result = validatePassword.execute("yadurachok")

        assertEquals(result.successful, false)
    }
}
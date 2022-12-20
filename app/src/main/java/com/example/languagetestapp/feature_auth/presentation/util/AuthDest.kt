package com.example.languagetestapp.feature_auth.presentation.util

sealed class AuthDest(val route: String) {
    object LoginDest: AuthDest(route = "login")
    object RegisterDest: AuthDest(route = "register")
}

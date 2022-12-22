package com.example.languagetestapp.feature_auth.data.local

import android.content.SharedPreferences

class AuthStorageGatewayImpl(
    private val prefs: SharedPreferences
): AuthStorageGateway {

    override fun storeAccessToken(accessToken: String) {
        prefs.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    override fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    override fun storeAccessTokenExp(exp: Long) {
        prefs.edit().putLong(ACCESS_TOKEN_EXP_KEY, exp).apply()
    }

    override fun fetchAccessTokenExp(): Long {
        return prefs.getLong(ACCESS_TOKEN_EXP_KEY, 0L)
    }

    override fun storeRefreshToken(refreshToken: String) {
        prefs.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
    }

    override fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }
}

const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
const val ACCESS_TOKEN_EXP_KEY = "ACCESS_TOKEN_EXP_KEY"
const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
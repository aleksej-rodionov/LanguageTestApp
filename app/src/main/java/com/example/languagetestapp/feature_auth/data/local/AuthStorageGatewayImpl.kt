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

    override fun clearAccessToken() {
        prefs.edit().remove(ACCESS_TOKEN_KEY).apply()
    }

    override fun storeAccessTokenExp(exp: Long) {
        prefs.edit().putLong(ACCESS_TOKEN_EXP_KEY, exp).apply()
    }

    override fun fetchAccessTokenExp(): Long {
        return prefs.getLong(ACCESS_TOKEN_EXP_KEY, 0L)
    }

    override fun clearAccessTokenExp() {
        prefs.edit().remove(ACCESS_TOKEN_EXP_KEY).apply()
    }

    override fun storeRefreshToken(refreshToken: String?) {
        refreshToken?.let {
            prefs.edit().putString(REFRESH_TOKEN_KEY, it).apply()
        }
    }

    override fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    override fun clearRefreshToken() {
        prefs.edit().remove(REFRESH_TOKEN_KEY).apply()
    }
}

const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
const val ACCESS_TOKEN_EXP_KEY = "ACCESS_TOKEN_EXP_KEY"
const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
package com.example.languagetestapp.feature_auth.data.local

import android.content.SharedPreferences
import com.example.languagetestapp.feature_auth.domain.model.User
import com.google.gson.Gson

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


    //====================STORING CURRENT USER DATA====================
    override fun storeCurrentUserData(user: User) { // todo stored with _id = null. Fix it?
        val userStringified = Gson().toJson(user, User::class.java)
        prefs.edit().putString(STRINGIFIED_USER_KEY, userStringified).apply()
    }

    override fun fetchCurrentUserData(): User? {
        val userStringified = prefs.getString(STRINGIFIED_USER_KEY, null)
        val user = userStringified?.let {
            Gson().fromJson(it, User::class.java)
        }
        return user
    }

    override fun clearCurrentUserData() {
        prefs.edit().remove(STRINGIFIED_USER_KEY).apply()
    }
}

const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
const val ACCESS_TOKEN_EXP_KEY = "ACCESS_TOKEN_EXP_KEY"
const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
const val STRINGIFIED_USER_KEY = "STRINGIFIED_USER_KEY"
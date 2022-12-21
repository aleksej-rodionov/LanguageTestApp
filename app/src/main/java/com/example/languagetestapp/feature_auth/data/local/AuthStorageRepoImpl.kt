package com.example.languagetestapp.feature_auth.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.languagetestapp.feature_auth.data.local.model.UserEntity
import com.example.languagetestapp.feature_auth.domain.repo.AuthStorageRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val TAG_AUTH_STORAGE = "AuthStorageImpl"

class AuthStorageRepoImpl @Inject constructor(
    private val authStorage: DataStore<Preferences>
): AuthStorageRepo {

    init {
        Log.d(TAG_AUTH_STORAGE, "authStorage.hashcode = ${this.hashCode()}")
    }

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val CURRENT_USER_KEY = stringPreferencesKey("current_user")
    }

    override suspend fun storeAccessToken(accessToken: String) {
        authStorage.edit { authdata ->
            authdata[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun fetchAccessToken(): Result<String> {
        return Result.runCatching {
            val flow = authStorage.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        Log.e(TAG_AUTH_STORAGE, "Error reading preferences", exception)
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { prefs ->
                    prefs[ACCESS_TOKEN_KEY]
                }
            val value = flow.firstOrNull() ?: ""
            value
        }
    }

    override suspend fun storeRefreshToken(refreshToken: String) {
        authStorage.edit { authdata ->
            authdata[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun fetchRefreshToken(): Result<String> {
        return Result.runCatching {
            val flow = authStorage.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        Log.e(TAG_AUTH_STORAGE, "Error reading preferences", exception)
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { prefs ->
                    prefs[REFRESH_TOKEN_KEY]
                }
            val value = flow.firstOrNull() ?: ""
            value
        }
    }

    override suspend fun storeCurrentUser(user: UserEntity) {
        val serializedUser = Gson().toJson(user, UserEntity::class.java)

        authStorage.edit { authdata ->
            authdata[CURRENT_USER_KEY] = serializedUser
        }
    }

    override suspend fun fetchCurrentUser(): Result<UserEntity> {
        return Result.runCatching {
            val flow = authStorage.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        Log.e(TAG_AUTH_STORAGE, "Error reading preferences", exception)
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { prefs ->
                    prefs[CURRENT_USER_KEY]
                }
            val value = flow.firstOrNull()
            val user = value?.let {
                Gson().fromJson(value, UserEntity::class.java)
            } ?: UserEntity("", "", "")
            user
        }
    }
}
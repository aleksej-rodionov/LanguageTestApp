package com.example.languagetestapp.feature_auth.di

import android.app.Application
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.BuildConfig
import com.example.languagetestapp.core.di.ApplicationScope
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.data.remote.LanguageUserApi
import com.example.languagetestapp.feature_auth.util.countExp
import com.example.languagetestapp.feature_notes.di.toRequestWithToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val TAG_USER_MODULE = "UserModule"

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    @User
    fun provideOkHttpUser(
        app: Application,
        authStorageGateway: AuthStorageGateway,
        authApi: LanguageAuthApi,
        @ApplicationScope appScope: CoroutineScope
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor {
            val originalRequest = it.request()

            val request = originalRequest.newBuilder()
                .method(originalRequest.method, originalRequest.body)
                .removeHeader(AUTHORIZATION).apply {
                    authStorageGateway.fetchAccessToken()?.let {
                        Log.d(TAG_USER_MODULE, "fetchAccessToken = ${it}")
                        addHeader(AUTHORIZATION, "$BEARER$it")
                    }
                }
                .build()

            it.proceed(request)
        }

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckerInterceptor(app.applicationContext))
        }

        builder.authenticator { _, response ->

            synchronized(this) {
                appScope.launch {
                    val refreshToken = authStorageGateway.fetchRefreshToken()
                    if (!refreshToken.isNullOrEmpty()) {

                        val newAccessTokenResponse = authApi.refresh(refreshToken)
                        if (newAccessTokenResponse.error.isNullOrEmpty()) {
                            val newAccessToken = newAccessTokenResponse.body
                            newAccessToken?.let {
                                // store new access token in shared
                                authStorageGateway.storeAccessToken(it.accessToken)
                                authStorageGateway.storeAccessTokenExp(it.countExp())
                                // change accessToken header
                                response.toRequestWithToken(it.accessToken)
                            }
                        } else {
                            authStorageGateway.clearAccessToken()
                            Log.d(TAG_USER_MODULE, newAccessTokenResponse.error
                                ?: "unknown error receiving new accessToken")
                        }
                    }
                }
            }
            null
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @User
    fun provideRetrofitUser(@User okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(@User retrofit: Retrofit): LanguageUserApi {
        return retrofit.create(LanguageUserApi::class.java)
    }




    const val BASE_URL = "http://192.168.16.102:3000/"
    const val BASE_URL_MACHINE = "http://10.0.2.2:3000/"
    const val BASE_URL_BRANCH = "http://192.168.1.239:3000/"

    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "
}
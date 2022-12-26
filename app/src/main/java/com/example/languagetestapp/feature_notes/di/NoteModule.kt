package com.example.languagetestapp.feature_notes.di

import android.app.Application
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.BuildConfig
import com.example.languagetestapp.core.di.ApplicationScope
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.util.Constants.TAG_AUTH
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.data.repo.NoteRepoImpl
import com.example.languagetestapp.feature_notes.di.NoteModule.AUTHORIZATION
import com.example.languagetestapp.feature_notes.di.NoteModule.BEARER
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val TAG_NOTE_MODULE = "NoteModule"

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    @Note
    fun provideOkHttpClientNote(
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
                        Log.d(TAG_NOTE_MODULE, "fetchAccessToken = ${it}")
                        addHeader(AUTHORIZATION, "$BEARER$it")
                    }
                }
                .build()

            it.proceed(request)
        }

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckerInterceptor(app.applicationContext))
        }

        // todo add Authenticator that fires AuthApi.refreshToken method
        builder.authenticator { _, response ->

            // todo how to make it synchronized?
            appScope.launch {
                val refreshToken = authStorageGateway.fetchRefreshToken()
                if (!refreshToken.isNullOrEmpty()) {
                    // make refreshToken call
                    val newAccessTokenResponse = authApi.refreshToken(refreshToken)

                    // todo change Responce model to sealed Resource<> in order to use 'when' instead 'if'?
                    if (newAccessTokenResponse.error.isNullOrEmpty()) {
                        val newAccessToken = newAccessTokenResponse.body
                        newAccessToken?.let {
                            // store new accessToken in shared
                            authStorageGateway.storeAccessToken(it)
                            // change accessToken header
                            response.toRequestWithToken(it)
                        }
                    } else {
                        // clear old accessToken anyway (to logout)
                        authStorageGateway.clearAccessToken()
                        Log.d(TAG_AUTH, newAccessTokenResponse.error ?:
                            "unknown error receiving new accessToken")
                    }
                }
            }

            null
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @Note
    fun provideRetrofitNote(@Note okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteApi(@Note retrofit: Retrofit): LanguageNoteApi {
        return retrofit.create(LanguageNoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoteRepo(noteApi: LanguageNoteApi): NoteRepo {
        return NoteRepoImpl(noteApi)
    }





    const val BASE_URL = "http://192.168.16.103:3000/"
    const val BASE_URL_MACHINE = "http://10.0.2.2:3000/"
    const val BASE_URL_BRANCH = "http://192.168.1.239:3000/"

    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "
}



//====================EXTENSIONS====================
fun Response.toRequestWithToken(token: String) =
    this.request.newBuilder()
        .removeHeader(AUTHORIZATION)
        .addHeader(AUTHORIZATION, BEARER + token)
        .build()
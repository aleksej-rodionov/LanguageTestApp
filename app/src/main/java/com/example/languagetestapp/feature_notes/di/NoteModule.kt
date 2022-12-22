package com.example.languagetestapp.feature_notes.di

import android.app.Application
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.BuildConfig
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.data.repo.NoteRepoImpl
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
        authStorageGateway: AuthStorageGateway
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

        // todo refresh token?

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
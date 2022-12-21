package com.example.languagetestapp.feature_notes.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

//    @Provides
//    @Singleton
//    @ApplicationScope
//    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    @Note
    fun provideOkHttpClientNote(
        app: Application,
        authStorageGateway: AuthStorageGateway
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
//            .connectTimeout(40, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS)
//            .writeTimeout(40, TimeUnit.SECONDS)

        builder.addInterceptor {
            val originalRequest = it.request()
            val request = originalRequest.newBuilder()
                .method(originalRequest.method, originalRequest.body)
                .removeHeader(AUTHORIZATION).apply {
                    authStorageGateway.fetchAccessToken()?.let {
                        addHeader(AUTHORIZATION, "$BEARER$it")
                    }
                }
                .build()

            it.proceed(request)
        }

        builder.addInterceptor(ChuckerInterceptor(app.applicationContext))

        builder.authenticator { _, response ->
            synchronized(this) {
                val refreshToken = authStorageGateway.fetchRefreshToken()
                val curAccessToken = authStorageGateway.fetchAccessToken()


            }
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @Note
    fun provideRetrofitNote(@Note okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_BRANCH)
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





    const val BASE_URL = "http://localhost:3000/"
    const val BASE_URL_MACHINE = "http://10.0.2.2:3000/"
    const val BASE_URL_BRANCH = "http://192.168.1.239:3000/"

    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "
}

//@Retention(AnnotationRetention.RUNTIME)
//@Qualifier
//annotation class ApplicationScope
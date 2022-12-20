package com.example.languagetestapp.feature_auth.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.data.repo.AuthRepoImpl
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_auth.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @Auth
    fun provideOkHttpClientAuth(app: Application): OkHttpClient {
        val builder = OkHttpClient.Builder()
//            .connectTimeout(40, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS)
//            .writeTimeout(40, TimeUnit.SECONDS)
        builder.addInterceptor(ChuckerInterceptor(app.applicationContext))
        return builder.build()
    }

    @Provides
    @Singleton
    @Auth
    fun provideRetrofitAuth(@Auth okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Auth
    fun provideAuthApi(@Auth retrofit: Retrofit): LanguageAuthApi {
        return retrofit.create(LanguageAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(authApi: LanguageAuthApi): AuthRepo {
        return AuthRepoImpl(authApi)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCases(authRepo: AuthRepo): AuthUseCases {
        return AuthUseCases(
            register = Register(authRepo),
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword(),
            validateRepeatedPassword = ValidateRepeatedPassword(),
            validateTerms = ValidateTerms()
        )
    }

//    @Provides
//    @Singleton
//    fun provideRegister(authRepo: AuthRepo): Register {
//        return Register(authRepo)
//    }

    const val AUTH_URL = "http://localhost:4000/"
}
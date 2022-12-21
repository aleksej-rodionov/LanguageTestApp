package com.example.languagetestapp.feature_notes.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.data.repo.NoteRepoImpl
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
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
object NoteModule {

    @Provides
    @Singleton
    @Note
    fun provideOkHttpClientNote(app: Application): OkHttpClient {
        val builder = OkHttpClient.Builder()
//            .connectTimeout(40, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS)
//            .writeTimeout(40, TimeUnit.SECONDS)
        builder.addInterceptor(ChuckerInterceptor(app.applicationContext))
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
}
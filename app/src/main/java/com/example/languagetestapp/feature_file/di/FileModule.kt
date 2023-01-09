package com.example.languagetestapp.feature_file.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.languagetestapp.BuildConfig
import com.example.languagetestapp.core.di.ApplicationScope
import com.example.languagetestapp.feature_file.data.remote.FileApi
import com.example.languagetestapp.feature_file.data.repo.FileRepoImpl
import com.example.languagetestapp.feature_file.data.repo.FileStatefulRepoImpl
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {

    @Provides
    @Singleton
    @File
    fun provideOkHttpClientFile(app: Application): OkHttpClient {
        val builder = OkHttpClient.Builder()
//            .connectTimeout(40, TimeUnit.SECONDS)
//            .readTimeout(40, TimeUnit.SECONDS)
//            .writeTimeout(40, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(ChuckerInterceptor(app.applicationContext))
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @File
    fun provideRetrofitFile(@File okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FILE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFileApi(@File retrofit: Retrofit): FileApi {
        return retrofit.create(FileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFileRepo(
        fileApi: FileApi,
        context: Application,
        fileStatefulRepo: FileStatefulRepo
    ): FileRepo = FileRepoImpl(fileApi, context, fileStatefulRepo)

    @Provides
    @Singleton
    @FileScope
    fun provideNoteScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideStatefulNoteRepo(@FileScope fileScope: CoroutineScope): FileStatefulRepo {
        return FileStatefulRepoImpl(fileScope)
    }




    //====================CONSTANTS====================
    const val FILE_URL = "http://192.168.16.104:3500/"
    const val FILE_URL_MACHINE = "http://10.0.2.2:3500/"
    const val FILE_URL_BRANCH = "http://192.168.1.239:3500/"
}
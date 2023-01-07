package com.example.languagetestapp.core.di

import android.app.Application
import com.example.languagetestapp.core.util.permission.PermissionHandler
import com.example.languagetestapp.feature_profile.presentation.util.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideFileManager(context: Application) = FileManager(context)

    @Provides
    @Singleton
    fun providePermissionHandler() = PermissionHandler()
}
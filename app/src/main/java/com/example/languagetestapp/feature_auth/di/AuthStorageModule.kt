package com.example.languagetestapp.feature_auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.languagetestapp.feature_auth.data.local.AuthStorageRepoImpl
import com.example.languagetestapp.feature_auth.domain.repo.AuthStorageRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.authStorage: DataStore<Preferences> by preferencesDataStore(
    name = "auth_storage"
)

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthStorageModule {

    companion object {
        // Provides instance of AuthDataStore
        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.authStorage
        }
    }

    // Binds instance of AuthStoreRepo
    @Binds
    @Singleton
    abstract fun bindAuthStorageRepo(
        authStorageRepoImpl: AuthStorageRepoImpl
    ): AuthStorageRepo
}
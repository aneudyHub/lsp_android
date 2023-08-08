package com.system.lsp.di

import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.repositories.PlatformSessionRepository
import com.system.lsp.data.repositories.PlatformSessionRepositoryImpl
import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.data.repositories.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun providesUserRepository(
        apiService: ApiService,
        userSessionSharedPreferences: UserSessionSharedPreferences
    ): UsersRepository {
        return UsersRepositoryImpl(apiService, userSessionSharedPreferences)
    }

    @Provides
    @Singleton
    fun providesPlatformSessionRepository(
        platformService: PlatformService,
        platformSessionSharedPreferences: PlatformSessionSharedPreferences,
        deviceId: String
    ): PlatformSessionRepository {
        return PlatformSessionRepositoryImpl(
            platformService,
            platformSessionSharedPreferences,
            deviceId
        )
    }
}
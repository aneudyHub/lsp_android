package com.system.lsp.di

import com.system.lsp.data.remote.repositories.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mockito.Mockito.mock
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UsersRepository {
        return mock(UsersRepository::class.java)
    }

}
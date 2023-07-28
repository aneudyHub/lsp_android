package com.system.lsp.di

import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.domain.GetCurrentUserUseCase
import com.system.lsp.domain.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun providesSignInUseCase(usersRepository: UsersRepository): SignInUseCase {
        return SignInUseCase(usersRepository)
    }

    @Provides
    @Singleton
    fun providesGetCurrentUserUseCase(usersRepository: UsersRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(usersRepository)
    }
}
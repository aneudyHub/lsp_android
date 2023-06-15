package com.system.lsp.di

import com.system.lsp.data.remote.repositories.UsersRepository
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
}
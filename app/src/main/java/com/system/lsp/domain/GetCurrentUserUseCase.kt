package com.system.lsp.domain

import com.system.lsp.data.local.models.User
import com.system.lsp.data.repositories.UsersRepository
import java.lang.Exception
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val usersRepository: UsersRepository) {
    operator fun invoke(): UseCaseResult<User?> {
        return try {
            val currentUser = usersRepository.currentUser
            UseCaseResult.Success(currentUser)
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrorType.THROWN_EXCEPTION)
        }
    }
}
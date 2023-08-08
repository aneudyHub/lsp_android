package com.system.lsp.domain

import com.system.lsp.data.repositories.UsersRepository
import javax.inject.Inject
import java.lang.Exception

class LogOutUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(): UseCaseResult<Unit?> {
        return try {
            usersRepository.logOut()
            UseCaseResult.Success(null)
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrorType.THROWN_EXCEPTION)
        }
    }
}
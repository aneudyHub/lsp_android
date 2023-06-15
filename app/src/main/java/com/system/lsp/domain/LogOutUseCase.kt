package com.system.lsp.domain

import com.system.lsp.data.remote.models.ErrorType
import com.system.lsp.data.remote.repositories.UsersRepository
import javax.inject.Inject
import com.system.lsp.data.remote.models.Result
import java.lang.Exception

class LogOutUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun execute(): Result<Unit?> {
        return try {
            usersRepository.logOut()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(ErrorType.THROWN_EXCEPTION)
        }
    }
}
package com.system.lsp.domain

import com.system.lsp.data.local.models.User
import com.system.lsp.data.remote.models.ErrorType
import com.system.lsp.data.remote.repositories.UsersRepository
import java.lang.Exception
import javax.inject.Inject
import com.system.lsp.data.remote.models.Result

class GetCurrentUserUseCase @Inject constructor(private val usersRepository: UsersRepository) {
    suspend fun execute(): Result<User?> {
        return try {
            val currentUser = usersRepository.currentUser
            Result.Success(currentUser)
        } catch (e: Exception) {
            Result.Error(ErrorType.THROWN_EXCEPTION)
        }
    }
}
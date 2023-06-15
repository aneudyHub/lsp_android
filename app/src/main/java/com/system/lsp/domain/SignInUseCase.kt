package com.system.lsp.domain

import com.system.lsp.data.remote.models.ErrorType
import com.system.lsp.data.remote.repositories.UsersRepository
import java.lang.Exception
import javax.inject.Inject
import com.system.lsp.data.remote.models.Result

class SignInUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun execute(userName: String, password: String): Result<Unit?> {
        return try {
            val response = usersRepository.signIn(userName, password)
            response
        } catch (e: Exception) {
            Result.Error(ErrorType.THROWN_EXCEPTION)
        }
    }
}
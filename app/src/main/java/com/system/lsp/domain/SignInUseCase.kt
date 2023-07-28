package com.system.lsp.domain

import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.repositories.UsersRepository
import java.lang.Exception
import javax.inject.Inject
import com.system.lsp.data.remote.models.Result

open class SignInUseCase @Inject constructor(
    private val usersRepository: UsersRepository?
) {
    suspend operator fun invoke(userName: String, password: String): UseCaseResult<Unit?> {
        return try {
            when (val response = usersRepository!!.signIn(userName, password)) {
                is Result.Success -> UseCaseResult.Success(response.data)
                is Result.Error -> {
                    val error = handleErrorResponse(response.error)
                    UseCaseResult.Error(error)
                }
            }
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrorType.THROWN_EXCEPTION)
        }
    }

    private fun handleErrorResponse(httpResponseErrorCode: HttpResponseErrorCode): UseCaseErrorType {
        return when (httpResponseErrorCode) {
            HttpResponseErrorCode.BAD_REQUEST,
            HttpResponseErrorCode.UNAUTHORIZED,
            HttpResponseErrorCode.FORBIDDEN -> UseCaseErrorType.USER_AND_PASS_WRONG

            HttpResponseErrorCode.INTERNAL_SERVER_ERROR -> UseCaseErrorType.SERVER_ERROR
            HttpResponseErrorCode.UNKNOWN, HttpResponseErrorCode.THROWN_EXCEPTION -> UseCaseErrorType.THROWN_EXCEPTION

            HttpResponseErrorCode.NOT_FOUND -> UseCaseErrorType.API_REQUEST_NOT_FOUND
        }
    }
}
package com.system.lsp.domain

import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.PlatformSessionRepository
import java.lang.Exception
import javax.inject.Inject

open class AuthenticatePlatformUseCase @Inject constructor(
    private val platformSessionRepository: PlatformSessionRepository
) {
    suspend operator fun invoke(hashCode: String): UseCaseResult<Unit?> {
        return try {
            when (val response = platformSessionRepository.authenticate(hashCode)) {
                is Result.Success -> UseCaseResult.Success(null)
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
            HttpResponseErrorCode.FORBIDDEN -> UseCaseErrorType.AUTHENTICATION_PLATFORM_FAILED

            HttpResponseErrorCode.INTERNAL_SERVER_ERROR -> UseCaseErrorType.SERVER_ERROR
            HttpResponseErrorCode.UNKNOWN, HttpResponseErrorCode.THROWN_EXCEPTION -> UseCaseErrorType.THROWN_EXCEPTION

            HttpResponseErrorCode.NOT_FOUND -> UseCaseErrorType.API_REQUEST_NOT_FOUND
        }
    }
}
package com.system.lsp.domain

import com.system.lsp.data.local.models.PlatformSession
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.PlatformSessionRepository
import javax.inject.Inject

class LoadPlatformDataUseCase @Inject constructor(
    private val platformSessionRepository: PlatformSessionRepository
) {
    suspend operator fun invoke(): UseCaseResult<PlatformSession?> {
        return try {
            when (val platformResponse = platformSessionRepository.loadPlatformSession()) {
                is Result.Success -> {
                    return UseCaseResult.Success(platformResponse.data)
                }

                is Result.Error -> {
                    val error = handleErrorResponse(platformResponse.error)
                    return UseCaseResult.Error(error)
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
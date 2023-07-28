package com.system.lsp.data.repositories

import com.system.lsp.data.local.models.PlatformSession
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.remote.extensions.handleErrorResponse
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.PlatformAuthorizationBody
import com.system.lsp.data.remote.models.Result
import java.lang.Exception
import javax.inject.Inject

class PlatformSessionRepositoryImpl @Inject constructor(
    private val platformService: PlatformService,
    private val platformSessionSharedPreferences: PlatformSessionSharedPreferences,
    private val deviceId: String
) : PlatformSessionRepository {

    override val apiUrl: String?
        get() = platformSessionSharedPreferences.apiUrl

    override suspend fun loadPlatformSession(): Result<PlatformSession?> {
        return try {
            val platformSession = platformSessionSharedPreferences.retrieveSession()
            Result.Success(platformSession)
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun authenticate(code: String): Result<Unit?> {
        return try {
            val body = PlatformAuthorizationBody(code, deviceId)
            val response = platformService.authorize(body)
            if (response.isSuccessful) {
                response.body()?.let {
                    platformSessionSharedPreferences.apiUrl = it.apiUrl
                }
                Result.Success(null)
            } else {
                val error = response.handleErrorResponse()
                Result.Error(error)
            }
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun dropSession(): Result<Unit?> {
        return try {
            platformSessionSharedPreferences.dropSession()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

}
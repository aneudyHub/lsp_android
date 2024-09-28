package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.extensions.handleErrorResponse
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.SyncDataPullBodyResponse
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest
import javax.inject.Inject

class SyncDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SyncDataRepository {
    override suspend fun sendData(body: SyncDataPushBodyRequest): Result<Unit?> {
        return try {
            val response = apiService.pushData(body)
            if (response.isSuccessful) {
                Result.Success(null)
            } else {
                val error = response.handleErrorResponse()
                Result.Error(error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun retrieveData(): Result<SyncDataPullBodyResponse> {
        return try {
            val response = apiService.pullData(syncTime = "0")
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                val error = response.handleErrorResponse()
                Result.Error(error)
            }
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }
}
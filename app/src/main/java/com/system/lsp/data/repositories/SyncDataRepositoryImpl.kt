package com.system.lsp.data.repositories

import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.extensions.handleErrorResponse
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.SyncDataPullBodyResponse
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest
import com.system.lsp.domain.model.SyncWorkerState
import com.system.lsp.domain.repository.SyncDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SyncDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SyncDataRepository {
    private val _syncState = MutableSharedFlow<SyncWorkerState>(replay = 1)
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

    override suspend fun emitSyncState(state: SyncWorkerState) {
        _syncState.emit(state)
    }

    override val syncState: SharedFlow<SyncWorkerState> = _syncState.asSharedFlow()
}
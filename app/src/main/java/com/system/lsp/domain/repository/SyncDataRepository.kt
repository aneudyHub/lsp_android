package com.system.lsp.domain.repository

import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.SyncDataPullBodyResponse
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest
import com.system.lsp.domain.model.SyncWorkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SyncDataRepository {
    suspend fun sendData(body: SyncDataPushBodyRequest): Result<Unit?>
    suspend fun retrieveData(): Result<SyncDataPullBodyResponse>
    suspend fun emitSyncState(state: SyncWorkerState)
    val syncState: SharedFlow<SyncWorkerState>
}
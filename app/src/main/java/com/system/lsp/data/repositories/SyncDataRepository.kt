package com.system.lsp.data.repositories

import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.SyncDataPullBodyResponse
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest

interface SyncDataRepository {
    suspend fun sendData(body: SyncDataPushBodyRequest): Result<Unit?>
    suspend fun retrieveData(): Result<SyncDataPullBodyResponse>
}
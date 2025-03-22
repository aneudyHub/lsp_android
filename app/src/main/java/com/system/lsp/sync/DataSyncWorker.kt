package com.system.lsp.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.system.lsp.domain.model.SyncWorkerState
import com.system.lsp.domain.repository.SyncDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val remoteSyncHandler: RemoteSyncHandler,
    @Assisted private val localSyncHandler: LocalSyncHandler,
    @Assisted private val syncDataRepository: SyncDataRepository
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "DataSyncWorker"
    override suspend fun doWork(): Result {
        Log.e(TAG, "doWork: DataSyncWorker")
        return try {
            val remoteResponse = remoteSyncHandler.run()
            syncDataRepository.emitSyncState(SyncWorkerState.Started)
            when (remoteResponse) {
                SyncResponse.Error -> {
                    // TODO: handle
                    syncDataRepository.emitSyncState(SyncWorkerState.Failure)
                    Result.retry()
                }

                SyncResponse.Success -> {
                    localSyncHandler.run()
                }
            }
            syncDataRepository.emitSyncState(SyncWorkerState.Success)
            Result.success()
        } catch (e: Exception) {
            syncDataRepository.emitSyncState(SyncWorkerState.Failure)
            Result.retry()
        }

    }
}
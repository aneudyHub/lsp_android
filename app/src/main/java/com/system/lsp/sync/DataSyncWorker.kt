package com.system.lsp.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val remoteSyncHandler: RemoteSyncHandler,
    @Assisted private val localSyncHandler: LocalSyncHandler
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "DataSyncWorker"
    override suspend fun doWork(): Result {
        Log.e(TAG, "doWork: DataSyncWorker")
        return try {
            val remoteResponse = remoteSyncHandler.run()
            when (remoteResponse) {
                SyncResponse.Error -> {
                    // TODO: handle
                    Result.retry()
                }

                SyncResponse.Success -> {
                    localSyncHandler.run()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }

    }
}
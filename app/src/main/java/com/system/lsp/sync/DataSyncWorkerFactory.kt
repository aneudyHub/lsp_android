package com.system.lsp.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.system.lsp.domain.repository.SyncDataRepository
import javax.inject.Inject

class DataSyncWorkerFactory @Inject constructor(
    private val remoteSyncHandler: RemoteSyncHandler,
    private val localSyncHandler: LocalSyncHandler,
    private val syncDataRepository: SyncDataRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DataSyncWorker::class.java.name ->
                DataSyncWorker(appContext, workerParameters, remoteSyncHandler, localSyncHandler, syncDataRepository)

            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}

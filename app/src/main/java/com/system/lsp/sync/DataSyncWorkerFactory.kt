package com.system.lsp.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class DataSyncWorkerFactory @Inject constructor(
    private val remoteSyncHandler: RemoteSyncHandler,
    private val localSyncHandler: LocalSyncHandler
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DataSyncWorker::class.java.name ->
                DataSyncWorker(appContext, workerParameters, remoteSyncHandler, localSyncHandler)

            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}

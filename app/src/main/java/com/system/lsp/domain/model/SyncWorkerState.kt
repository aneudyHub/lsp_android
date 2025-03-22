package com.system.lsp.domain.model

sealed class SyncWorkerState {
    object Started : SyncWorkerState()
    object Success : SyncWorkerState()
    object Failure : SyncWorkerState()
}
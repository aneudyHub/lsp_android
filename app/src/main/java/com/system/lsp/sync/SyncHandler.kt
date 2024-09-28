package com.system.lsp.sync

abstract class SyncHandler {
    abstract suspend fun run(): SyncResponse
}
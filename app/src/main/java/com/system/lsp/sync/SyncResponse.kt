package com.system.lsp.sync

sealed class SyncResponse{
    object Success : SyncResponse()
    object Error: SyncResponse()
}
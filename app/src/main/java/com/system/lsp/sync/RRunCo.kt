package com.system.lsp.sync

import com.system.lsp.data.repositories.SyncDataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun run(syncDataRepository: SyncDataRepository? = null){
    GlobalScope.launch {
        syncDataRepository?.retrieveData()
    }
}
package com.system.lsp.data.repositories

import com.system.lsp.data.local.models.PlatformSession
import com.system.lsp.data.remote.models.Result

interface PlatformSessionRepository {
    val apiUrl: String?
    suspend fun loadPlatformSession(): Result<PlatformSession?>
    suspend fun authenticate(code: String): Result<Unit?>
    suspend fun dropSession(): Result<Unit?>
}
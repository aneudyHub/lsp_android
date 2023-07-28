package com.system.lsp.data.local.sharedpreferences

import com.system.lsp.data.local.models.PlatformSession

interface PlatformSessionSharedPreferences {
    var apiUrl: String?
    fun saveSession(platformSession: PlatformSession)
    fun retrieveSession(): PlatformSession?
    fun dropSession()
}
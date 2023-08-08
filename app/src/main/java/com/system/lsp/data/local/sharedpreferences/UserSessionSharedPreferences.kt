package com.system.lsp.data.local.sharedpreferences

import com.system.lsp.data.local.models.User

interface UserSessionSharedPreferences {
    var token: String?
    fun saveUser(user: User)
    fun retrieveUser(): User?
    fun dropSession()
}
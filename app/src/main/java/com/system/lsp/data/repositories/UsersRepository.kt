package com.system.lsp.data.repositories

import com.system.lsp.data.local.models.User
import com.system.lsp.data.remote.models.Result

interface UsersRepository {
    val currentUser: User?
    suspend fun signIn(userName: String, password: String): Result<Unit?>
    suspend fun logOut(): Result<Unit?>
}
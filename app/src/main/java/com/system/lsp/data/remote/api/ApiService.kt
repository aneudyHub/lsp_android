package com.system.lsp.data.remote.api

import com.system.lsp.data.remote.models.ApiResponse
import com.system.lsp.data.remote.models.LoginResponse
import retrofit2.http.POST

interface ApiService {

    @POST("usuarios/signIn")
    suspend fun signIn(userName: String, password: String): ApiResponse<LoginResponse>
}
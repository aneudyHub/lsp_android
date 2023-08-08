package com.system.lsp.data.remote.api

import com.system.lsp.data.remote.models.LoginResponse
import com.system.lsp.data.remote.models.LoginUserBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("usuarios/login")
    suspend fun signIn(@Body user: LoginUserBody): Response<LoginResponse>
}
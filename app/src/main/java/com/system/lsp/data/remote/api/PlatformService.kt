package com.system.lsp.data.remote.api

import com.system.lsp.data.remote.models.PlatformAuthorizationBody
import com.system.lsp.data.remote.models.PlatformAuthorizationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PlatformService {

    @POST("dispositivos/authorize")
    suspend fun authorize(@Body platformAuthorizationBody: PlatformAuthorizationBody): Response<PlatformAuthorizationResponse>
}
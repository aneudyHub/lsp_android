package com.system.lsp.data.remote.api

import com.system.lsp.data.remote.models.LoginResponse
import com.system.lsp.data.remote.models.LoginUserBody
import com.system.lsp.data.remote.models.SyncDataPullBodyResponse
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("usuarios/login")
    suspend fun signIn(@Body user: LoginUserBody): Response<LoginResponse>

    @POST("sync")
    suspend fun pushData(@Body syncDataPushBodyRequest: SyncDataPushBodyRequest): Response<Nothing>

    @GET("sync")
    suspend fun pullData(@Header("sync_time") syncTime: String): Response<SyncDataPullBodyResponse>
}
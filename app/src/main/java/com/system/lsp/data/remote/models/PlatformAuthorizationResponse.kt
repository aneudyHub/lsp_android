package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class PlatformAuthorizationResponse(
    @SerializedName("connection")
    val apiUrl: String
)
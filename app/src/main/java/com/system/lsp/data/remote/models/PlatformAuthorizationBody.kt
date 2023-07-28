package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class PlatformAuthorizationBody(
    @SerializedName("code")
    val hashCode: String,
    val imei: String
)
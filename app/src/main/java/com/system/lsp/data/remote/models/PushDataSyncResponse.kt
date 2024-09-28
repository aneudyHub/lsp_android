package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class PushDataSyncResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)

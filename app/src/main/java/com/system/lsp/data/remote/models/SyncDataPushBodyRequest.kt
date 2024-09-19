package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class SyncDataPushBodyRequest(
    @SerializedName("inserciones")
    val insertions: ArrayList<PagoSyncBody>
)

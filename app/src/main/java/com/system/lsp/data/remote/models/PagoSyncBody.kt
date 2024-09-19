package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class PagoSyncBody(
    @SerializedName("usuarios_id")
    val userId: String,
    @SerializedName("fecha")
    val date: String,
    @SerializedName("prestamos_id")
    val loanId: Int,
    @SerializedName("monto")
    val amount: Double,
    @SerializedName("note")
    val note: String
)

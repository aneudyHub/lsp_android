package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val id: Long,
    @SerializedName("firstname")
    val firstName: String,
    @SerializedName("lastname")
    val lastName: String,
    val email: String,
    @SerializedName("celular")
    val phone: String,
    val token: String
)
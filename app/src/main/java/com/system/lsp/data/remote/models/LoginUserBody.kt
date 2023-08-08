package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class LoginUserBody(
    @SerializedName("id") val userName: String,
    val password: String
)

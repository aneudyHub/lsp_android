package com.system.lsp.data.remote.models

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ApiError?
)
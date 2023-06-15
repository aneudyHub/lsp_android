package com.system.lsp.data.remote.models

enum class ErrorType {
    USER_OR_PASS_WRONG,
    SERVER_OUTAGE,
    THROWN_EXCEPTION
}

const val UNAUTHORIZED = 401
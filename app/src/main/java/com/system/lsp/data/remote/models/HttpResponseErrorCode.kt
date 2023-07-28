package com.system.lsp.data.remote.models

enum class HttpResponseErrorCode {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
    UNKNOWN,
    THROWN_EXCEPTION
}
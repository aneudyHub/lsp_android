package com.system.lsp.data.remote.extensions

import com.system.lsp.data.remote.models.HttpResponseErrorCode
import retrofit2.Response


fun <T> Response<T>.handleErrorResponse(): HttpResponseErrorCode {
    val error = when (code()) {
        400 -> HttpResponseErrorCode.BAD_REQUEST
        401 -> HttpResponseErrorCode.UNAUTHORIZED
        403 -> HttpResponseErrorCode.FORBIDDEN
        404 -> HttpResponseErrorCode.NOT_FOUND
        500 -> HttpResponseErrorCode.INTERNAL_SERVER_ERROR
        else -> HttpResponseErrorCode.UNKNOWN
    }
    return error
}
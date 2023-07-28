package com.system.lsp.domain

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val errorType: UseCaseErrorType) : UseCaseResult<Nothing>()
}
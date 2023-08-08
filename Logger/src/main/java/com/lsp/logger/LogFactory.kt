package com.lsp.logger

interface LogFactory {
    fun logError(tag: String, message: String, extras: Map<String, Any>? = null)
    fun logWarning(tag: String, message: String, extras: Map<String, Any>? = null)
    fun logInfo(tag: String, message: String, extras: Map<String, Any>? = null)
    fun logDebug(tag: String, message: String, extras: Map<String, Any>? = null)
}
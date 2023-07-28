package com.lsp.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class LogFactoryImpl @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics
) : LogFactory {

    override fun logError(tag: String, message: String, extras: Map<String, Any>?) {
        log(LogLevel.ERROR, tag, message, extras)
    }

    override fun logWarning(tag: String, message: String, extras: Map<String, Any>?) {
        log(LogLevel.WARNING, tag, message, extras)
    }

    override fun logInfo(tag: String, message: String, extras: Map<String, Any>?) {
        log(LogLevel.INFO, tag, message, extras)
    }

    override fun logDebug(tag: String, message: String, extras: Map<String, Any>?) {
        log(LogLevel.DEBUG, tag, message, extras)
    }

    private fun log(
        level: LogLevel,
        tag: String,
        message: String,
        extras: Map<String, Any>? = null
    ) {
        val logMessage = buildLogMessage(level, tag, message, extras)
        // Replace the following line with the actual logging implementation, e.g., printing to console or writing to a log file.

        when (level) {
            LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING -> {
                firebaseCrashlytics.log(logMessage)
                System.out.println(logMessage)
            }

            LogLevel.ERROR -> {
                System.err.println(logMessage)
                val exception = Exception(logMessage)
                firebaseCrashlytics.recordException(exception)
            }
        }

    }

    private fun buildLogMessage(
        level: LogLevel,
        tag: String,
        message: String,
        extras: Map<String, Any>?
    ): String {
        val logBuilder = StringBuilder()
        logBuilder.append("[${level.name}] $tag: $message")

        extras?.let {
            logBuilder.append("( ")
            for ((key, value) in it) {
                logBuilder.append("$key : $value")
            }
            logBuilder.append(" )")
        }

        return logBuilder.toString()
    }

}

enum class LogLevel {
    ERROR,
    WARNING,
    INFO,
    DEBUG
}
package com.system.lsp.ui.extensions

import com.system.lsp.R
import com.system.lsp.data.remote.models.ErrorType


fun ErrorType.getStringResource(): Int {
    return when (this) {
        ErrorType.USER_OR_PASS_WRONG -> R.string.api_error_user_pass_are_bad
        ErrorType.SERVER_OUTAGE -> R.string.api_error_server_outage
        ErrorType.THROWN_EXCEPTION -> R.string.thrown_exception
    }
}

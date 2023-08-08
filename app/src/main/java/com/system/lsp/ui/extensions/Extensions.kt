package com.system.lsp.ui.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.system.lsp.R
import com.system.lsp.domain.UseCaseErrorType


fun UseCaseErrorType.getStringResource(): Int {
    return when (this) {
        UseCaseErrorType.USER_AND_PASS_WRONG -> R.string.api_error_user_pass_are_bad
        UseCaseErrorType.SERVER_ERROR,
        UseCaseErrorType.API_REQUEST_NOT_FOUND -> R.string.api_error_server_outage

        UseCaseErrorType.THROWN_EXCEPTION -> R.string.thrown_exception
        UseCaseErrorType.AUTHENTICATION_PLATFORM_FAILED -> R.string.authentication_platform_failed
    }
}


fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = currentFocus
    if (currentFocusView != null) {
        imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
    }
}

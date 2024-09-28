package com.system.lsp.data.remote.network

import android.util.Log
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor(
    private val userSessionSharedPreferences: UserSessionSharedPreferences,
    private val logoutCallback: LogoutCallback
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code() == 401) {
            // Trigger logout
            userSessionSharedPreferences.dropSession()
            logoutCallback.onLogout()
            Log.e("NETWORK", response.code().toString())
            // Optionally, you could throw an exception or return an empty response
        }

        return response
    }
}
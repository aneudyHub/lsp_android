package com.system.lsp.data.remote.network

import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    val userSessionSharedPreferences: UserSessionSharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = userSessionSharedPreferences.token ?: ""
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}
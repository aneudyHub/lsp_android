package com.system.lsp.data.local.sharedpreferences

import android.content.SharedPreferences
import com.system.lsp.data.local.models.PlatformSession
import javax.inject.Inject

class PlatformSessionSharedPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PlatformSessionSharedPreferences {

    override var apiUrl: String?
        get() {
            return sharedPreferences.getString(KEY_API_URL, null)
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_API_URL, value).apply()
        }

    override fun saveSession(platformSession: PlatformSession) {
        with(sharedPreferences.edit()) {
            putString(KEY_HASH_CODE_AUTHENTICATOR, platformSession.hashCodeAuthenticator)
            putString(KEY_COMPANY_NAME, platformSession.companyName)
            putString(KEY_COMPANY_ADDRESS, platformSession.companyAddress)
            putString(KEY_COMPANY_PHONE, platformSession.companyPhone)
        }.apply()
    }

    override fun retrieveSession(): PlatformSession? {
        if (apiUrl.isNullOrBlank()) return null

        return with(sharedPreferences) {
            val hashCode = getString(KEY_HASH_CODE_AUTHENTICATOR, "")
            val companyName = getString(KEY_COMPANY_NAME, "")
            val companyAddress = getString(KEY_COMPANY_ADDRESS, "")
            val companyPhone = getString(KEY_COMPANY_PHONE, "")
            PlatformSession(hashCode, companyName, companyAddress, companyPhone, apiUrl)
        }
    }

    override fun dropSession() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_HASH_CODE_AUTHENTICATOR = "hash_code"
        private const val KEY_COMPANY_NAME = "company_name"
        private const val KEY_COMPANY_ADDRESS = "company_address"
        private const val KEY_COMPANY_PHONE = "company_phone"
        private const val KEY_API_URL = "api_url"
    }
}
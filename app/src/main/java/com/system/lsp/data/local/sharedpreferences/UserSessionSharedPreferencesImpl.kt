package com.system.lsp.data.local.sharedpreferences

import android.content.SharedPreferences
import com.system.lsp.data.local.models.User
import javax.inject.Inject

class UserSessionSharedPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserSessionSharedPreferences {

    override var token: String?
        get() {
            return sharedPreferences.getString(KEY_TOKEN, null)
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_TOKEN, value).apply()
        }

    override fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_FIRST_NAME, user.firstName)
            putString(KEY_USER_LAST_NAME, user.lastName)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_PHONE, user.phone)
        }.apply()
    }

    override fun retrieveUser(): User? {
        if (token.isNullOrBlank()) return null

        return with(sharedPreferences) {
            val id = getInt(KEY_USER_ID, 0)
            val firstName = getString(KEY_USER_FIRST_NAME, DEFAULT_VALUE)
            val lastName = getString(KEY_USER_LAST_NAME, DEFAULT_VALUE)
            val email = getString(KEY_USER_EMAIL, DEFAULT_VALUE)
            val phone = getString(KEY_USER_PHONE, DEFAULT_VALUE)
            User(id, firstName, lastName, email, phone)
        }
    }

    override fun dropSession() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "user_token"
        private const val DEFAULT_VALUE = ""
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_FIRST_NAME = "user_first_name"
        private const val KEY_USER_LAST_NAME = "user_last_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
    }
}
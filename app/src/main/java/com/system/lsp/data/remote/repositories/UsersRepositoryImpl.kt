package com.system.lsp.data.remote.repositories

import com.system.lsp.data.local.models.User
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.models.ErrorType
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.UNAUTHORIZED
import java.lang.Exception
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userSessionSharedPreferences: UserSessionSharedPreferences
) : UsersRepository {


    override val currentUser: User?
        get() = userSessionSharedPreferences.retrieveUser()


    override suspend fun signIn(userName: String, password: String): Result<Unit?> {
        try {
            val response = apiService.signIn(userName, password)
            return if (response.success) {
                response.data?.let { data ->
                    userSessionSharedPreferences.token = data.token
                    val user = User(
                        id = data.id,
                        firstName = data.firstName,
                        lastName = data.lastName,
                        email = data.email,
                        phone = data.phone
                    )
                    userSessionSharedPreferences.saveUser(user)
                }
                Result.Success(null)
            } else {
                val error = when (response.error?.code) {
                    UNAUTHORIZED -> ErrorType.USER_OR_PASS_WRONG
                    else -> ErrorType.SERVER_OUTAGE
                }
                Result.Error(error)
            }
        } catch (e: Exception) {
            return Result.Error(ErrorType.THROWN_EXCEPTION)
        }
    }

    override suspend fun logOut(): Result<Unit?> {
        return try {
            userSessionSharedPreferences.dropSession()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(ErrorType.THROWN_EXCEPTION)
        }
    }
}
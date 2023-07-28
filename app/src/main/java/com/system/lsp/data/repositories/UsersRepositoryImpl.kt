package com.system.lsp.data.repositories

import com.system.lsp.data.local.models.User
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.extensions.handleErrorResponse
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.LoginUserBody
import com.system.lsp.data.remote.models.Result
import java.lang.Exception
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userSessionSharedPreferences: UserSessionSharedPreferences
) : UsersRepository {


    override val currentUser: User?
        get() = userSessionSharedPreferences.retrieveUser()


    override suspend fun signIn(userName: String, password: String): Result<Unit?> {
        return try {
            val user = LoginUserBody(userName, password)
            val response = apiService.signIn(user)
            if (response.isSuccessful) {
                response.body()?.let { data ->
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
                val error = response.handleErrorResponse()
                Result.Error(error)
            }
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun logOut(): Result<Unit?> {
        return try {
            userSessionSharedPreferences.dropSession()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }
}
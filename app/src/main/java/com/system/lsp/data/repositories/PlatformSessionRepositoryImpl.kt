package com.system.lsp.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.system.lsp.data.local.models.PlatformSession
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.remote.extensions.handleErrorResponse
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.PlatformAuthorizationBody
import com.system.lsp.data.remote.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class PlatformSessionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val platformSessionSharedPreferences: PlatformSessionSharedPreferences,
    private val deviceId: String
) : PlatformSessionRepository {

    override val apiUrl: String?
        get() = platformSessionSharedPreferences.apiUrl

    override suspend fun loadPlatformSession(): Result<PlatformSession?> {
        return try {
            val platformSession = platformSessionSharedPreferences.retrieveSession()
            Result.Success(platformSession)
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun authenticate(code: String): Result<Unit?> {
        return try {
            val documentSnapshot = firestore
                .collection("systems_auth_codes")
                .whereEqualTo("code", code)
                .get()
                .await()

            if (documentSnapshot.isEmpty) {
                return Result.Error(HttpResponseErrorCode.UNAUTHORIZED)
            }
            documentSnapshot?.let {
                platformSessionSharedPreferences.apiUrl =
                    it.documents[0].data?.getValue("url").toString()
            }

            return Result.Success(null)
        } catch (e: Exception) {
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

    override suspend fun dropSession(): Result<Unit?> {
        return try {
            platformSessionSharedPreferences.dropSession()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }

}
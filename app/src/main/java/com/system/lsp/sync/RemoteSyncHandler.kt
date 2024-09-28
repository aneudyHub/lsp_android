package com.system.lsp.sync

import android.util.Log
import com.system.lsp.data.remote.models.PagoSyncBody
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.remote.models.SyncDataPushBodyRequest
import com.system.lsp.data.repositories.PaymentsRepository
import com.system.lsp.data.repositories.SyncDataRepository
import com.system.lsp.data.repositories.UsersRepository
import javax.inject.Inject

class RemoteSyncHandler @Inject constructor(
    private val syncDataRepository: SyncDataRepository,
    private var paymentsRepository: PaymentsRepository,
    private val usersRepository: UsersRepository
) : SyncHandler() {

    private val TAG = RemoteSyncHandler::class.java.simpleName
    override suspend fun run(): SyncResponse {
        Log.e(TAG, "inicio sendRequest")
        val currentUser = usersRepository.currentUser ?: return SyncResponse.Error
        val paymentsResponse = paymentsRepository.getNoSyncedPayments(currentUser.id)
        val requestBody = when (paymentsResponse) {
            is Result.Error -> {
                SyncDataPushBodyRequest()
            }

            is Result.Success -> {
                val insertions = paymentsResponse.data.map { paymentWithDetails ->
                    PagoSyncBody(
                        userId = paymentWithDetails.paymentEntity?.userId!!,
                        date = paymentWithDetails.paymentEntity.date.toString(),
                        loanId = paymentWithDetails.paymentEntity.loanId?.toInt()!!,
                        amount = paymentWithDetails.paymentDetailsEntity?.sumOf { it.capital!! + it.interest!! + it.delayInterest!! }
                            ?: 0.0,
                        note = ""
                    )
                }

                SyncDataPushBodyRequest(
                    insertions = insertions.toList()
                )
            }
        }

        val syncResponse = syncDataRepository.sendData(requestBody)

        return when (syncResponse) {
            is Result.Error -> {
                Log.e(TAG, "Error sending data")
                SyncResponse.Error
            }

            is Result.Success -> {
                SyncResponse.Success
            }
        }
    }
}
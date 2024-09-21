package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.entities.relations.PaymentWithDetails
import com.system.lsp.data.remote.models.Result

interface PaymentsRepository {
    suspend fun pay()
    suspend fun getNoSyncedPayments(userId: Long): Result<List<PaymentWithDetails>>
}
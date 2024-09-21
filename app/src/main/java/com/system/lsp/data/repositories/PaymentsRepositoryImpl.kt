package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.entities.relations.PaymentWithDetails
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import javax.inject.Inject

class PaymentsRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentsDao,
    private val paymentDetailDao: PaymentDetailDao
): PaymentsRepository {
    override suspend fun pay() {
        TODO("Not yet implemented")
    }

    override suspend fun getNoSyncedPayments(userId: Long): Result<List<PaymentWithDetails>> {
        try {
            val payments = paymentDao.getNoSyncedPayments(userId)
            return Result.Success(payments)
        }catch (e: Exception){
            return Result.Error(HttpResponseErrorCode.THROWN_EXCEPTION)
        }
    }
}
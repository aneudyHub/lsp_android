package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.PaymentEntity
import com.system.lsp.data.local.database.entities.PaymentWithCustomerAndPaymentDetails
import com.system.lsp.data.local.database.entities.PaymentWithDetails
import java.util.Date

@Dao
interface PaymentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg paymentEntity: PaymentEntity)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_TABLE_NAME}")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where id=:id")
    suspend fun getById(id: Long): PaymentWithDetails
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where userId=:userId and date=:sortDate")
    suspend fun getListByUserId(userId: Long, sortDate: Date? = null): List<PaymentWithDetails>
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where id=:id")
    suspend fun getPaymentsWithDetailsById(id: Long): PaymentWithCustomerAndPaymentDetails
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where loanId=:loanId")
    suspend fun getPaymentsWithDetailsByLoanId(loanId: Long): PaymentWithCustomerAndPaymentDetails
}
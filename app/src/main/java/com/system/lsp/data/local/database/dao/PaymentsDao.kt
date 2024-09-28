package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.PaymentEntity
import com.system.lsp.data.local.database.entities.relations.PaymentWithDetails
import java.util.Date

@Dao
interface PaymentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg paymentEntity: PaymentEntity)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_TABLE_NAME}")
    suspend fun deleteAll()

    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME}")
    suspend fun getAll(): List<PaymentEntity>

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where id=:id")
    suspend fun getById(id: Long): PaymentWithDetails

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where userId=:userId and date=:sortDate")
    suspend fun getListByUserIdAndDate(
        userId: Long,
        sortDate: Long
    ): List<PaymentWithDetails>

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where userId=:userId")
    suspend fun getListByUserId(userId: Long): List<PaymentWithDetails>

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where loanId=:loanId")
    suspend fun getPaymentsWithDetailsByLoanId(loanId: Long): List<PaymentWithDetails>

    @Transaction
    @Query("SELECT * FROM ${AppDatabase.PAYMENTS_TABLE_NAME} where isSynced=0 and userId=:userId")
    suspend fun getNoSyncedPayments(userId: Long): List<PaymentWithDetails>
}
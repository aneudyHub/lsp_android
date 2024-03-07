package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.PaymentDetailsEntity


@Dao
interface PaymentDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg paymentDetailsEntity: PaymentDetailsEntity)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_DETAILS_TABLE_NAME} where paymentId=:paymentId")
    suspend fun deleteAllByPaymentId(paymentId: Long)

    @Query("DELETE FROM ${AppDatabase.PAYMENTS_DETAILS_TABLE_NAME}")
    suspend fun deleteAll()
}
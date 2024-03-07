package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.CustomerWithLoans
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.LoanWithDetails

@Dao
interface LoansDao {

    @Query("SELECT * FROM ${AppDatabase.LOANS_TABLE_NAME} where id=:id")
    suspend fun getById(id: Long): LoanEntity

    @Query("SELECT * FROM ${AppDatabase.LOANS_TABLE_NAME} where isDeleted = 0")
    suspend fun getAll(): List<LoanEntity>
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.LOANS_TABLE_NAME} WHERE customerId=:customerId")
    suspend fun getLoansByCustomer(customerId: Long): CustomerWithLoans

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg loanEntity: LoanEntity)

    @Query("DELETE FROM ${AppDatabase.LOANS_TABLE_NAME} where id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${AppDatabase.LOANS_TABLE_NAME}")
    suspend fun deleteAll()
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.LOANS_TABLE_NAME} where id=:id")
    suspend fun getLoanWithDetailsById(id: Long): LoanWithDetails
}
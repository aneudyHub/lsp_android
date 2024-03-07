package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.LoansDetailsEntity

@Dao
interface LoanDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg loansDetailsEntity: LoansDetailsEntity)

    @Query("SELECT * FROM ${AppDatabase.LOANS_DETAILS_TABLE_NAME} where id=:id")
    suspend fun getById(id: Long): LoansDetailsEntity?

    @Query("DELETE FROM ${AppDatabase.LOANS_DETAILS_TABLE_NAME} where id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${AppDatabase.LOANS_DETAILS_TABLE_NAME} where loanId=:loanId")
    suspend fun deleteByLoanId(loanId: Long)

    @Query("DELETE FROM ${AppDatabase.LOANS_DETAILS_TABLE_NAME}")
    suspend fun deleteAll()
}
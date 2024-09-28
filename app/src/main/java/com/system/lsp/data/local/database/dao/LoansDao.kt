package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.ExpiredLoanDetail
import com.system.lsp.data.local.database.entities.relations.CustomerWithLoans
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.relations.LoanWithDetails
import com.system.lsp.utilidades.UTiempo
import java.sql.Date

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(loans: List<LoanEntity>)

    @Query("DELETE FROM ${AppDatabase.LOANS_TABLE_NAME} where id=:id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${AppDatabase.LOANS_TABLE_NAME}")
    suspend fun deleteAll()
    @Transaction
    @Query("SELECT * FROM ${AppDatabase.LOANS_TABLE_NAME} where id=:id")
    suspend fun getLoanWithDetailsById(id: Long): LoanWithDetails

//    @Transaction
//    @Query("SELECT p.id as loanId, count(pd.quota) as qoutes FROM ${AppDatabase.LOANS_TABLE_NAME} as p join ${AppDatabase.LOANS_DETAILS_TABLE_NAME} as pd on p.id = pd.loanId join ${AppDatabase.CUSTOMERS_TABLE_NAME} as cli on p.customerId = cli.id where pd.isPaid = 0 group by p.id order by pd.dueDate DESC")
//    suspend fun getAllLoansWithDetails(): List<LoanWithDetails>

    @Query("""
        SELECT l.id AS loanId, c.name AS customerName, ld.*
        FROM ${AppDatabase.LOANS_DETAILS_TABLE_NAME} ld
        JOIN ${AppDatabase.LOANS_TABLE_NAME} l ON ld.loanId = l.id
        JOIN ${AppDatabase.CUSTOMERS_TABLE_NAME} c ON l.customerId = c.id
        WHERE ld.isPaid = 0
    """)
    suspend fun getExpiredUnpaidLoans(): List<ExpiredLoanDetail>

}
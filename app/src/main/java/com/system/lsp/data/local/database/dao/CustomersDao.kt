package com.system.lsp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.entities.CustomerEntity

@Dao
interface CustomersDao {

    @Query("SELECT * FROM ${AppDatabase.CUSTOMERS_TABLE_NAME}")
    suspend fun getAll(): List<CustomerEntity>

    @Query("SELECT * FROM ${AppDatabase.CUSTOMERS_TABLE_NAME} where id=:id")
    suspend fun getById(id: Long): CustomerEntity

    @Query("SELECT * FROM ${AppDatabase.CUSTOMERS_TABLE_NAME} WHERE documentId = :documentId")
    suspend fun getByDocumentId(documentId: String): CustomerEntity

    @Query("SELECT * FROM ${AppDatabase.CUSTOMERS_TABLE_NAME} WHERE documentId LIKE '%' || :keyword || '%' OR name LIKE '%' || :keyword || '%' OR lastName LIKE '%' || :keyword || '%' OR address LIKE '%' || :keyword || '%' OR phoneNumber LIKE '%' || :keyword || '%'")
    suspend fun searchByKeyword(keyword: String): List<CustomerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdate(vararg customerEntity: CustomerEntity)

    @Query("DELETE FROM ${AppDatabase.CUSTOMERS_TABLE_NAME} WHERE id = :customerId")
    suspend fun deleteById(customerId: Long)

    @Query("DELETE FROM ${AppDatabase.CUSTOMERS_TABLE_NAME}")
    suspend fun deleteAll()

}
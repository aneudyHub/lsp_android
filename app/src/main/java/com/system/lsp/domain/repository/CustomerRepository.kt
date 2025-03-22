package com.system.lsp.domain.repository

import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.models.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun getAll(): Result<List<CustomerEntity>>
    suspend fun filterByCriteria(criteria: String): Result<List<CustomerEntity>>
    suspend fun create(customer: Customer): Result<Nothing>
    suspend fun delete(id: Long): Result<Nothing>
    suspend fun getById(id: Long): Result<CustomerEntity>
    suspend fun update(customer: Customer): Result<Nothing>
}
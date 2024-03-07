package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.remote.models.Result
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun getAll(): Result<List<CustomerEntity>>
}
package com.system.lsp.data.remote.datasources

import com.system.lsp.data.repositories.models.Customer

interface CustomersRemoteDataSource {
    suspend fun create(customer: Customer): Long
}
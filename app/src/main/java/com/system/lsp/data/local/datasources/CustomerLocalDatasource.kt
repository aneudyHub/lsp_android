package com.system.lsp.data.local.datasources

import com.system.lsp.data.local.database.entities.CustomerEntity

interface CustomerLocalDatasource {
    suspend fun getAll(): List<CustomerEntity>
}
package com.system.lsp.data.remote.datasources

import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.repositories.models.Customer
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CustomersRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val coroutineContext: CoroutineContext
): CustomersRemoteDataSource {
    override suspend fun create(customer: Customer): Long {
        TODO()
    }
}
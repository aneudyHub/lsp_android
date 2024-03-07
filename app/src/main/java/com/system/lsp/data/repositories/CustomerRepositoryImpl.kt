package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.datasources.CustomerLocalDatasource
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CustomerRepositoryImpl @Inject constructor(
    val customersDao: CustomersDao,
    val coroutineContext: CoroutineContext
) : CustomerRepository {
    override suspend fun getAll(): Result<List<CustomerEntity>> {
        return withContext(coroutineContext) {
            try {
                val list = customersDao.getAll()
                Result.Success(list)
            } catch (e: Exception) {
                Result.Error(HttpResponseErrorCode.UNKNOWN)
            }
        }
    }

}
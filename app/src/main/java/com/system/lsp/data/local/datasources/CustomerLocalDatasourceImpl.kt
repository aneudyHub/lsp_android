package com.system.lsp.data.local.datasources

import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CustomerLocalDatasourceImpl @Inject constructor(
    val customersDao: CustomersDao,
    val coroutineDispatcher: CoroutineContext
): CustomerLocalDatasource {
    override suspend fun getAll(): List<CustomerEntity> {
        return withContext(coroutineDispatcher){
            customersDao.getAll()
        }
    }
}
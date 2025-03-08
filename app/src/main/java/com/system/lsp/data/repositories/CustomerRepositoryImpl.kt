package com.system.lsp.data.repositories

import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.datasources.CustomerLocalDatasource
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.models.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CustomerRepositoryImpl @Inject constructor(
    private val customersDao: CustomersDao,
    private val coroutineContext: CoroutineContext
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

    override suspend fun filterByCriteria(criteria: String): Result<List<CustomerEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(customer: Customer): Result<Nothing> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Result<Nothing> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): Result<CustomerEntity> {
        return withContext(coroutineContext){
            try {
                val customer = customersDao.getById(id)
                Result.Success(customer)
            } catch (e:Exception){
                Result.Error(HttpResponseErrorCode.UNKNOWN)
            }
        }
    }

    override suspend fun update(customer: Customer): Result<Nothing> {
        TODO("Not yet implemented")
    }

}
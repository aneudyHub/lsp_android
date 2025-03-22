package com.system.lsp.sync

import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.LoansDetailsEntity
import com.system.lsp.data.remote.models.Result
import com.system.lsp.domain.repository.SyncDataRepository
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class LocalSyncHandler @Inject constructor(
    private val syncDataRepository: SyncDataRepository,
    private val loansDao: LoansDao,
    private val loanDetailsDao: LoanDetailsDao,
    private val customersDao: CustomersDao
) : SyncHandler() {


    override suspend fun run(): SyncResponse {
        return try {
            when (val response = syncDataRepository.retrieveData()) {
                is Result.Error -> {
                    SyncResponse.Error
                }

                is Result.Success -> {
                    val customers = response.data.customers.map { customer ->
                        CustomerEntity(
                            id = customer.id.toLong(),
                            name = customer.fullName,
                            documentId = customer.document,
                            phoneNumber = customer.phone,
                            pictureUrl = customer.photo,
                            address = customer.address,
                            location = "${customer.latitude},${customer.latitude}",
                        )
                    }
                    customersDao.insertCustomersBatch(customers)


                    val loans = response.data.loans.map { loan ->

                        LoanEntity(
                            id = loan.id.toLong(),
                            customerId = loan.clientId.toLong(),
                            capitalAmount = loan.capital,
                            interestPercentage = loan.interestPercentage,
                            delayInterestPercentage = loan.defaultInterestPercentage,
                            termType = loan.term,
                            quotes = loan.quotes,
//                        createdDate ="",
//                        startDate = "",
//                        updatedAt = ""
                            isPaid = loan.paidOff,
//                        endDate = ""
                            createdBy = ""
                        )
                    }

                    loansDao.insertBatch(loans)

                    val loanDetails = response.data.loanQuotes.map {
                        LoansDetailsEntity(
                            id = it.id.toLong(),
                            loanId = it.loanId.toLong(),
                            capital = it.capital,
                            interest = it.interest,
                            delayInterest = it.delayInterest,
                            delayInterestPaid = it.defaultInterestPayment,
                            dueDate = it.date.toSqlDate(),
                            paidDate = it.paidDate?.toSqlDate(),
                            paidAmount = it.amountPaid,
                            isPaid = it.isPaid,
                            quota = it.installmentNumber,
                        )
                    }

                    loanDetailsDao.insertBatch(loanDetails)




                    println("terminoooooo")
                    val a = customersDao.getAll()
                    println(a)

                    val b = loansDao.getAll()
                    println(b)

                    val c = loanDetailsDao.getAll()
                    println(c)


                    val x = b.filter { loan -> loan.customerId !in a.map { it.id } }
                    println("result fiter" + x)
                    println(x.size)

                    val v = response.data.customers.filter { it.id == 903 }

                    println(v)

                    println("clientes remote " + response.data.customers.size + " local + " + a.size)
                    println("prestamos remote " + response.data.loans.size + " local + " + b.size)
                    println("cuotas remote " + response.data.loanQuotes.size + " local + " + c.size)



                    SyncResponse.Success
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SyncResponse.Error
        }
    }
}

fun String.toSqlDate(): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val utilDate = format.parse(this) // Parse as java.util.Date
        utilDate?.let { Date(it.time) } // Convert to java.sql.Date
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
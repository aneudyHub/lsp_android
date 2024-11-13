package com.system.lsp.data.local.daos

import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.PaymentEntity
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.sql.Date
import java.time.LocalDate
import java.time.ZoneId
import java.util.Random
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class PaymentsRepositoryDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var paymentsDao: PaymentsDao

    @Inject
    lateinit var loansDao: LoansDao

    @Inject
    lateinit var customersDao: CustomersDao


    @Before
    fun setup() {
        hiltRule.inject()
    }

    private suspend fun registerMockLoan(): Long {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val loanRandomId = Random().nextLong().absoluteValue

        val loan = LoanEntity(
            id = loanRandomId,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date.valueOf("2022-01-01"),
            createdDate = Date.valueOf("2022-01-01"),
            updatedAt = Date.valueOf("2022-01-01")
        )

        loansDao.insertAndUpdate(loan)

        return loanRandomId
    }

    @Test
    fun testInsertAndGetPayment() = runTest {
        val loanId = registerMockLoan()

        val payment = PaymentEntity(
            id = 1,
            date = Date.valueOf("2022-01-01"),
            userId = 1,
            loanId = loanId
        )
        paymentsDao.insertAndUpdate(payment)

        val queryResult = paymentsDao.getById(1)
        Assert.assertEquals(payment, queryResult.paymentEntity)
    }

    @Test
    fun testDeletePayments() = runTest {
        val loanId = registerMockLoan()

        val payment = PaymentEntity(
            id = 1,
            date = Date.valueOf("2022-01-01"),
            userId = 1,
            loanId = loanId
        )
        paymentsDao.insertAndUpdate(payment)
        val queryResult = paymentsDao.getById(1)
        Assert.assertEquals(payment, queryResult.paymentEntity)

        paymentsDao.deleteById(1)
        val queryResultAfterDeleted = paymentsDao.getById(1)
        Assert.assertNull(queryResultAfterDeleted)
    }

    @Test
    fun testWipePaymentsTable() = runTest {
        val loanId = registerMockLoan()
        for (i in 1..10) {
            val payment = PaymentEntity(
                id = i.toLong(),
                date = Date.valueOf("2022-01-01"),
                userId = 1,
                loanId = loanId
            )
            paymentsDao.insertAndUpdate(payment)
        }

        val paymentsResult = paymentsDao.getAll()
        Assert.assertNotEquals(0, paymentsResult.size)

        paymentsDao.deleteAll()

        val paymentsResultAfterDeleted = paymentsDao.getAll()
        Assert.assertEquals(0, paymentsResultAfterDeleted.size)


    }

    @Test
    fun testGetListByUserId() = runTest {
        val loanId = registerMockLoan()
        val userId: Long = 1
        for (i in 1..10) {
            val localDate = LocalDate.of(2023, 1, 1 + i)
            val date = Date.valueOf("2023-01-01")
            val payment = PaymentEntity(
                id = i.toLong(),
                date = date,
                userId = userId,
                loanId = loanId
            )
            paymentsDao.insertAndUpdate(payment)
        }
        val paymentsResult = paymentsDao.getListByUserId(userId)
        Assert.assertNotEquals(0, paymentsResult.size)

        // filtering by Date
        var localDate = LocalDate.of(2023, 1, 1)
        var date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        var paymentsFilterResult = paymentsDao.getListByUserIdAndDate(userId, date.time)
        Assert.assertEquals(0, paymentsFilterResult.size)

        localDate = LocalDate.of(2023, 1, 2)
        date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        paymentsFilterResult = paymentsDao.getListByUserIdAndDate(userId, date.time)
        Assert.assertEquals(1, paymentsFilterResult.size)
    }


    @Test
    fun testGetPaymentsByLoan() = runTest {
        val loanList = mutableListOf<Long>()
        runBlocking {
            for (i in 1..10) {
                val loanId = registerMockLoan()
                loanList.add(loanId)
                for (a in 1..5) {
                    val payment = PaymentEntity(
                        id = Random().nextLong().absoluteValue,
                        date = Date.valueOf("2022-01-01"),
                        userId = 1,
                        loanId = loanId
                    )
                    paymentsDao.insertAndUpdate(payment)
                }
            }
        }
        val firstResultByLoan = paymentsDao.getPaymentsWithDetailsByLoanId(loanList[0])
        Assert.assertEquals(5, firstResultByLoan.size)

        val secondResultByLoan = paymentsDao.getPaymentsWithDetailsByLoanId(loanList[1])
        Assert.assertEquals(5, secondResultByLoan.size)
    }
}
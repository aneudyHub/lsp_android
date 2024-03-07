package com.system.lsp.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.LoansDetailsEntity
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class LoansDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var loansDao: LoansDao

    @Inject
    lateinit var customersDao: CustomersDao

    @Inject
    lateinit var loanDetailsDao: LoanDetailsDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testInsertAndGetLoans() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val loan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(loan)
        val result = loansDao.getById(loan.id!!)
        Assert.assertEquals(loan, result)
    }

    @Test
    fun testUpdateLoans() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val loan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(loan)

        val result = loansDao.getById(loan.id!!)

        Assert.assertEquals(loan, result)

        val updatedLoan = loan.copy(capitalAmount = 12000.00, termType = "D")

        loansDao.insertAndUpdate(updatedLoan)

        val resultAfterUpdate = loansDao.getById(updatedLoan.id!!)

        Assert.assertEquals(resultAfterUpdate, updatedLoan)
    }

    @Test
    fun testGetCustomerWithLoans() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val firstLoan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        val secondLoan = LoanEntity(
            id = 2,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(firstLoan)
        loansDao.insertAndUpdate(secondLoan)

        val customerWithLoans = loansDao.getLoansByCustomer(customer.id!!)

        Assert.assertEquals(customerWithLoans.customerEntity.id, customer.id)
        Assert.assertEquals(2, customerWithLoans.loans.size)
    }

    @Test
    fun testGetList() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val firstLoan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(firstLoan)

        val secondLoan = LoanEntity(
            id = 2,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )


        loansDao.insertAndUpdate(secondLoan)

        val thirdLoan = LoanEntity(
            id = 3,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(thirdLoan)

        val list = loansDao.getAll()

        Assert.assertEquals(3, list.size)
    }

    @Test
    fun testDeleteLoans() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val firstLoan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        val secondLoan = LoanEntity(
            id = 2,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(firstLoan)
        loansDao.insertAndUpdate(secondLoan)


        loansDao.deleteById(1)

        val result = loansDao.getAll()
        Assert.assertEquals(1, result.size)


        val thirdLoan = LoanEntity(
            id = 3,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(thirdLoan)

        loansDao.deleteAll()

        val listAfterDeleted = loansDao.getAll()

        Assert.assertEquals(0, listAfterDeleted.size)
    }

    @Test
    fun testGetLoanWithDetails() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)

        val loan = LoanEntity(
            id = 1,
            customerId = customer.id,
            capitalAmount = 10000.00,
            interestPercentage = 24.0f,
            delayInterestPercentage = 1.0f,
            termType = "M",
            quotes = 10,
            startDate = Date(),
            createdDate = Date(),
            updatedAt = Date()
        )

        loansDao.insertAndUpdate(loan)

        val firstQuote = LoansDetailsEntity(
            id = 1,
            loanId = loan.id,
            capital = 1000.00,
            interest = 100.00,
            delayInterest = 0.00,
            paidAmount = 0.00
        )

        loanDetailsDao.insertAndUpdate(firstQuote)

        val loanWithDetails = loansDao.getLoanWithDetailsById(loan.id!!)

        Assert.assertEquals(loanWithDetails.loan.id, loan.id)
        Assert.assertEquals(1, loanWithDetails.details.size)

    }
}
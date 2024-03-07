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
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class LoanDetailsDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var loanDetailsDao: LoanDetailsDao

    @Inject
    lateinit var loansDao: LoansDao

    @Inject
    lateinit var customersDao: CustomersDao

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testInsertAndGetDetails() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)


        val loan = LoanEntity(
            id = 1,
            capitalAmount = 10000.00,
            interestPercentage = 20.0f,
            customerId = customer.id
        )
        loansDao.insertAndUpdate(loan)

        val loanDetail = LoansDetailsEntity(
            id = 1,
            loanId = loan.id,
            capital = 1000.00,
            interest = 100.00,
            delayInterest = 0.00,
            paidAmount = 0.00
        )

        loanDetailsDao.insertAndUpdate(loanDetail)

        val detailResult = loanDetailsDao.getById(1)

        Assert.assertEquals(detailResult?.id, loanDetail.id)

    }

    @Test
    fun testUpdateDetails() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)


        val loan = LoanEntity(
            id = 1,
            capitalAmount = 10000.00,
            interestPercentage = 20.0f,
            customerId = customer.id
        )
        loansDao.insertAndUpdate(loan)

        val loanDetail = LoansDetailsEntity(
            id = 1,
            loanId = loan.id,
            capital = 1000.00,
            interest = 100.00,
            delayInterest = 0.00,
            paidAmount = 0.00
        )

        loanDetailsDao.insertAndUpdate(loanDetail)

        val detailResult = loanDetailsDao.getById(1)

        Assert.assertEquals(detailResult?.id, loanDetail.id)


        val updatedLoanDetail = loanDetail.copy(capital = 1200.00)

        loanDetailsDao.insertAndUpdate(updatedLoanDetail)

        val updatedDetailResult = loanDetailsDao.getById(1)

        Assert.assertEquals(updatedDetailResult?.id, updatedLoanDetail.id)
    }

    @Test
    fun testDeleteDetails() = runTest {
        val customer = CustomerEntity(
            id = 1,
            name = "Alex",
            lastName = "gomez",
            address = ""
        )
        customersDao.insertAndUpdate(customer)


        val loan = LoanEntity(
            id = 1,
            capitalAmount = 10000.00,
            interestPercentage = 20.0f,
            customerId = customer.id
        )
        loansDao.insertAndUpdate(loan)

        val loanDetail = LoansDetailsEntity(
            id = 1,
            loanId = loan.id,
            capital = 1000.00,
            interest = 100.00,
            delayInterest = 0.00,
            paidAmount = 0.00
        )

        loanDetailsDao.insertAndUpdate(loanDetail)

        val detailResult = loanDetailsDao.getById(1)

        Assert.assertEquals(detailResult?.id, loanDetail.id)

        loanDetailsDao.deleteAll()

        val list = loanDetailsDao.getById(loanDetail.id!!)

        Assert.assertNull(list)

    }

}
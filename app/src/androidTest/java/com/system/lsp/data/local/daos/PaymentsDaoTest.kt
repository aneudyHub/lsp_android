package com.system.lsp.data.local.daos

import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.entities.PaymentEntity
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class PaymentsDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var paymentsDao: PaymentsDao

    @Inject
//    lateinit var paymentDetailDao: PaymentDetailDao

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testInsertAndGetPayment() = runTest{
        val payment = PaymentEntity(
            date = Date(),
            userId = 1,
            loanId = 1
        )
        paymentsDao.insertAndUpdate(payment)

    }

    @Test
    fun testDeletePayments() = runTest {

    }

    @Test
    fun testGetListByUserId() = runTest {

    }
}
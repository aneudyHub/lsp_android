package com.system.lsp.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CustomersDaoTest {

    @Inject
    lateinit var customersDao: CustomersDao


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val customerMock = CustomerEntity(
        1,
        "aneudy",
        "vargas",
        "000000000000",
        "0000000000",
        "https://foto.png",
        "Calle principal #5, urb. caonabo",
        "0.00,0.00",
        Date(),
        isInserted = false,
        isUpdated = false,
        isDeleted = false
    )

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testInsertAndGetCustomer() = runTest {
        customersDao.insertAndUpdate(customerMock)

        val customerSaved = customersDao.getById(1)

        Assert.assertEquals(customerMock, customerSaved)
    }

    @Test
    fun testUpdateCustomer() = runTest {
        customersDao.insertAndUpdate(customerMock)

        val customerSaved = customersDao.getById(customerMock.id!!)

        Assert.assertEquals(customerMock, customerSaved)

        val customerUpdated = customerMock.copy(name = "Jose Luis", lastName = "Thais")

        customersDao.insertAndUpdate(customerUpdated)

        val customerAfterUpdated = customersDao.getById(customerUpdated.id!!)

        Assert.assertEquals(customerUpdated, customerAfterUpdated)
    }

    @Test
    fun testDeleteById() = runTest {
        customersDao.insertAndUpdate(customerMock)

        val list = customersDao.getAll()

        Assert.assertEquals(1, list.size)

        customersDao.deleteById(customerMock.id!!)

        val listAfterDeleted = customersDao.getAll()

        Assert.assertEquals(0, listAfterDeleted.size)
    }

    @Test
    fun testDeleteAll() = runTest {
        for (i in 0..10) {
            val customer = CustomerEntity(
                id = i.toLong(),
                name = "juan",
                lastName = "lopez"
            )

            customersDao.insertAndUpdate(customer)

        }
        val list = customersDao.getAll()
        Assert.assertNotEquals(0, list.size)

        customersDao.deleteAll()

        val listAfterDeleteAll = customersDao.getAll()

        Assert.assertEquals(0, listAfterDeleteAll.size)
    }


    @Test
    fun testSearchByKeyword() = runTest {
        val list = mutableListOf<CustomerEntity>()

        list.add(
            CustomerEntity(
                1,
                "aneudy",
                "vargas",
                "40223011293",
                "8093962555",
                "https://foto.png",
                "Calle principal #5, urb. caonabo",
                "0.00,0.00",
                null,
                isInserted = false,
                isUpdated = false,
                isDeleted = false
            )
        )

        list.add(
            CustomerEntity(
                2,
                "kevin",
                "vargas",
                "05600833080",
                "0000000000",
                "https://foto.png",
                "Calle j #3 , las flores",
                "0.00,0.00",
                null,
                isInserted = false,
                isUpdated = false,
                isDeleted = false
            )
        )

        list.add(
            CustomerEntity(
                3,
                "Maria",
                "Lopez",
                "40225963781",
                "8092445528",
                "https://foto.png",
                "Calle antonio guzman fernandez #2",
                "0.00,0.00",
                null,
                isInserted = false,
                isUpdated = false,
                isDeleted = false
            )
        )

        list.add(
            CustomerEntity(
                4,
                "jose",
                "alberto",
                "05697853692",
                "8096937894",
                "https://foto.png",
                "Calle k #25",
                "0.00,0.00",
                null,
                isInserted = false,
                isUpdated = false,
                isDeleted = false
            )
        )

        for (customer in list) {
            customersDao.insertAndUpdate(customer)
        }

        Assert.assertEquals(4 , customersDao.getAll().size)

        // filter by name = aneudy , it should returns just 1 result

        val firstFilter = customersDao.searchByKeyword("aneudy")
        Assert.assertEquals(1, firstFilter.size)


        // filter by lastname = vargas , it should returns just 2 result

        val secondFilter = customersDao.searchByKeyword("vargas")
        Assert.assertEquals(2, secondFilter.size)


        // filter by documentID = 40223011293 , it should returns just 1 result

        val thirdFilter = customersDao.searchByKeyword("40223011293")
        Assert.assertEquals(1, thirdFilter.size)


        // filter by address starting with Calle , it should returns just 4 result

        val forthFilter = customersDao.searchByKeyword("Calle")
        Assert.assertEquals(4, forthFilter.size)


        // filter by phone number = 8093962555, it should returns just 1 result

        val fifthFilter = customersDao.searchByKeyword("8093962555")
        Assert.assertEquals(1, fifthFilter.size)


    }

}
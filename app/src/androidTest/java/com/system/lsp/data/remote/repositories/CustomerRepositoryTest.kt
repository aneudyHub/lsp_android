package com.system.lsp.data.remote.repositories

import androidx.test.runner.AndroidJUnit4
import com.google.gson.Gson
import com.system.lsp.data.repositories.CustomerRepository
import com.system.lsp.di.AppModule
import com.system.lsp.di.DatabaseModule
import com.system.lsp.di.NetworkModule
import com.system.lsp.di.RepositoriesModule
import com.system.lsp.di.SyncModule
import com.system.lsp.di.UseCasesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(
    AppModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    SyncModule::class,
    UseCasesModule::class
)
class CustomerRepositoryTest {

    private val gson = Gson()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()

    }

    @Test
    fun createCustomerTest() = runTest{

    }

    @Test
    fun getListOfAllCustomerTest() = runTest {

    }

    @Test
    fun retrieveFilteredListByCriteriaTest() = runTest {

    }

    @Test
    fun deleteCustomerByIdTest() = runTest {

    }

    @Test
    fun getCustomerByIdTest() = runTest {

    }

    @Test
    fun updateCustomerTest() = runTest{

    }
}
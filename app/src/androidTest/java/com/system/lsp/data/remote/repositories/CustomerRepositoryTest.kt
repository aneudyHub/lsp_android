package com.system.lsp.data.remote.repositories

import androidx.test.runner.AndroidJUnit4
import com.system.lsp.data.repositories.CustomerRepository
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class CustomerRepositoryTest {

}
package com.system.lsp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.domain.repository.CustomerRepository
import com.system.lsp.data.repositories.CustomerRepositoryImpl
import com.system.lsp.domain.repository.PaymentsRepository
import com.system.lsp.data.repositories.PaymentsRepositoryImpl
import com.system.lsp.domain.repository.PlatformSessionRepository
import com.system.lsp.data.repositories.PlatformSessionRepositoryImpl
import com.system.lsp.domain.repository.SyncDataRepository
import com.system.lsp.data.repositories.SyncDataRepositoryImpl
import com.system.lsp.domain.repository.UsersRepository
import com.system.lsp.data.repositories.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoriesTestModule {
    @Provides
    @Singleton
    fun providesUserRepository(
        apiService: ApiService,
        userSessionSharedPreferences: UserSessionSharedPreferences
    ): UsersRepository {
        return UsersRepositoryImpl(apiService, userSessionSharedPreferences)
    }

    @Provides
    @Singleton
    fun providesPlatformSessionRepository(
        firestore: FirebaseFirestore,
        platformSessionSharedPreferences: PlatformSessionSharedPreferences,
        deviceId: String
    ): PlatformSessionRepository {
        return PlatformSessionRepositoryImpl(
            firestore,
            platformSessionSharedPreferences,
            deviceId
        )
    }

    @Provides
    @Singleton
    fun providesCustomerRepository(
        customersDao: CustomersDao
    ): CustomerRepository {
        return CustomerRepositoryImpl(customersDao, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun providesSyncDataRepository(apiService: ApiService): SyncDataRepository {
        return SyncDataRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesPaymentsRepository(
        paymentsDao: PaymentsDao,
        paymentDetailDao: PaymentDetailDao
    ): PaymentsRepository {
        return PaymentsRepositoryImpl(paymentsDao, paymentDetailDao)
    }
}
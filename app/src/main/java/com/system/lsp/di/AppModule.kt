package com.system.lsp.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.datasources.CustomerLocalDatasource
import com.system.lsp.data.local.datasources.CustomerLocalDatasourceImpl
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferencesImpl
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferencesImpl
import com.system.lsp.domain.repository.PaymentsRepository
import com.system.lsp.domain.repository.SyncDataRepository
import com.system.lsp.domain.repository.UsersRepository
import com.system.lsp.data.utils.getDeviceId
import com.system.lsp.sync.RemoteSyncHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("LSP_LOCAL_STORAGE", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideUserSharedPreferences(sharedPreferences: SharedPreferences): UserSessionSharedPreferences {
        return UserSessionSharedPreferencesImpl(sharedPreferences)
    }

    @Provides
    fun providePlatformSessionSharedPreferences(sharedPreferences: SharedPreferences): PlatformSessionSharedPreferences {
        return PlatformSessionSharedPreferencesImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesDeviceId(@ApplicationContext context: Context): String {
        return getDeviceId(context)
    }

    @Provides
    fun provideCustomerLocalDatasource(customersDao: CustomersDao): CustomerLocalDatasource {
        return CustomerLocalDatasourceImpl(customersDao, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }


    @Provides
    @Singleton
    fun provideRemoteSyncHandler(
        syncDataRepository: SyncDataRepository,
        paymentsRepository: PaymentsRepository,
        usersRepository: UsersRepository
    ): RemoteSyncHandler {
        return RemoteSyncHandler(syncDataRepository, paymentsRepository, usersRepository)
    }

    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}
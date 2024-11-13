package com.system.lsp.di

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.system.lsp.BuildConfig
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.AppDatabase.Companion.DATABASE_NAME
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.datasources.CustomerLocalDatasource
import com.system.lsp.data.local.datasources.CustomerLocalDatasourceImpl
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferencesImpl
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferencesImpl
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.repositories.PaymentsRepository
import com.system.lsp.data.repositories.SyncDataRepository
import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.data.utils.getDeviceId
import com.system.lsp.sync.RemoteSyncHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }


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
    fun providesCustomersDao(appDatabase: AppDatabase): CustomersDao {
        return appDatabase.customersDao()
    }

    @Provides
    fun providesLoansDao(appDatabase: AppDatabase): LoansDao {
        return appDatabase.loansDao()
    }

    @Provides
    @Singleton
    fun providesLoanDetailsDao(appDatabase: AppDatabase): LoanDetailsDao {
        return appDatabase.loansDetailsDao()
    }

    @Provides
    @Singleton
    fun providesPaymentsDao(appDatabase: AppDatabase): PaymentsDao {
        return appDatabase.paymentsDao()
    }

    @Provides
    @Singleton
    fun providesPaymentsDetailsDao(appDatabase: AppDatabase): PaymentDetailDao {
        return appDatabase.paymentsDetailsDao()
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
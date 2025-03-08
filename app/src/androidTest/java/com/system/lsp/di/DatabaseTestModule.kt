package com.system.lsp.di

import android.content.Context
import androidx.room.Room
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.utils.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseTestModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext, AppDatabase::class.java
        ).allowMainThreadQueries().addTypeConverter(Converters::class).build()
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

}
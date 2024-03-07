package com.system.lsp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferencesImpl
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferencesImpl
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.utils.getDeviceId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    private const val BASE_URL = "http://127.0.0.1:8080"
    private const val PLATFORM_BASE_URL = "http://127.0.0.1:8080"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext, AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesMockWebServer(): MockWebServer {
        return MockWebServer()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("apiService")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @Named("platformService")
    fun providePlatformRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(PLATFORM_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiService(@Named("apiService") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    fun providePlatformService(@Named("platformService") retrofit: Retrofit): PlatformService {
        return retrofit.create(PlatformService::class.java)
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("test", Context.MODE_PRIVATE)
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

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun providesCustomersDao(appDatabase: AppDatabase): CustomersDao {
        return appDatabase.customersDao()
    }

    @Provides
    @Singleton
    fun providesLoansDao(appDatabase: AppDatabase): LoansDao {
        return appDatabase.loansDao()
    }

    @Provides
    @Singleton
    fun providesLoanDetailsDao(appDatabase: AppDatabase): LoanDetailsDao{
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
package com.system.lsp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.system.lsp.data.local.database.AppDatabase
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.utils.Converters
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

    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
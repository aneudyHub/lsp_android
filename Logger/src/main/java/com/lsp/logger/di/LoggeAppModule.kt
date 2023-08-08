package com.lsp.logger.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lsp.logger.LogFactory
import com.lsp.logger.LogFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoggeAppModule {
    const val FB_CKEY_BUSINESS_NAME = "BUSINESS_NAME"

    @Provides
    @Singleton
    fun providesLogFactory(
        @ApplicationContext context: Context
    ): LogFactory {
        FirebaseApp.initializeApp(context)
        val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(true)
        return LogFactoryImpl(firebaseCrashlytics)
    }
}


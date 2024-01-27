package com.lsp.printer.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.SharedPreferences
import com.lsp.logger.LogFactory
import com.lsp.printer.Bluetooth.BluetoothFactory
import com.lsp.printer.Bluetooth.BluetoothFactoryImpl
import com.lsp.printer.data.local.PrinterLocalDataSource
import com.lsp.printer.data.local.PrinterLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrinterModule {

    @Provides
    @Singleton
    fun providesBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }

    @Provides
    @Singleton
    fun providesBluetoothFactory(
        bluetoothAdapter: BluetoothAdapter,
        @ApplicationContext context: Context,
        logger: LogFactory
    ): BluetoothFactory {
        return BluetoothFactoryImpl(bluetoothAdapter, context, logger)
    }

    @Provides
    @Singleton
    fun providesPrinterLocalDataSource(sharedPreferences: SharedPreferences): PrinterLocalDataSource{
        return PrinterLocalDataSourceImpl(sharedPreferences)
    }
}

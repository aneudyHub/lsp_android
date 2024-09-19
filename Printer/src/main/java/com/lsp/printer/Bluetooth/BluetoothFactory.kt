package com.lsp.printer.Bluetooth

import android.bluetooth.BluetoothDevice
import com.lsp.printer.printer.Printer
import kotlinx.coroutines.flow.Flow

interface BluetoothFactory {
    val isEnabled: Boolean
    val isSupported: Boolean
    val enableActionIntent: String
    suspend fun discoverDevices(): List<BluetoothDevice>
    suspend fun connectToDevice(device: BluetoothDevice)

    fun isDeviceConnected(): Boolean
    fun print(buffer: ByteArray): Flow<PrintingState>
    fun getPrinterFactory(): Printer
}
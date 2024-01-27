package com.lsp.printer.Bluetooth

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

interface BluetoothFactory {
    val isEnabled: Boolean
    val isSupported: Boolean
    val enableActionIntent: String
    suspend fun discoverDevices(): List<BluetoothDevice>
}
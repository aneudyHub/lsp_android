package com.lsp.printer.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat.registerReceiver
import com.lsp.logger.LogFactory
import com.lsp.printer.PrinterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.Logger
import javax.inject.Inject

class BluetoothFactoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context,
    private val logger: LogFactory
): BluetoothFactory {

    private val TAG = BluetoothFactory::class.java.simpleName

    override val isEnabled: Boolean
        get() = bluetoothAdapter.isEnabled
    override val isSupported: Boolean
        get() = bluetoothAdapter != null
    override val enableActionIntent: String
        get() = BluetoothAdapter.ACTION_REQUEST_ENABLE

    @SuppressLint("MissingPermission")
    override suspend fun discoverDevices(): List<BluetoothDevice> {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val discoveredDevices = mutableListOf<BluetoothDevice>()

        val discoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        if (device != null) {
                            discoveredDevices.add(device)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)

        // Start device discovery
        bluetoothAdapter.startDiscovery()

        // Suspend until the discovery is complete (this could be a long-running operation)
        withContext(Dispatchers.IO) {
            // Optionally, you can delay for a certain period or use a timeout
            // to control how long you want to run the discovery process.
            // delay(10000) // e.g., 10 seconds
            // Don't forget to unregister the receiver when done.
            delay(DISCOVERY_DEVICES_REGISTER_TIMEOUT)
            context.unregisterReceiver(discoveryReceiver)
        }
        return discoveredDevices
    }


    companion object{
        const val DISCOVERY_DEVICES_REGISTER_TIMEOUT = 10000L
    }

}
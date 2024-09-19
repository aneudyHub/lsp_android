package com.lsp.printer.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.lsp.logger.LogFactory
import com.lsp.printer.printer.CPCLPrinter
import com.lsp.printer.printer.Printer
import com.lsp.printer.printer.ZPLPrinter
import com.lsp.printer.printer.utils.TextSizeConverter
import com.zebra.sdk.printer.PrinterLanguage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.util.concurrent.Flow
import javax.inject.Inject

class BluetoothFactoryImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context,
    private val logger: LogFactory
) : BluetoothFactory {

    private val TAG = BluetoothFactory::class.java.simpleName

    override val isEnabled: Boolean
        get() = bluetoothAdapter.isEnabled
    override val isSupported: Boolean
        get() = bluetoothAdapter != null
    override val enableActionIntent: String
        get() = BluetoothAdapter.ACTION_REQUEST_ENABLE

    private lateinit var printerConnectionHandler: PrinterConnectionHandler

    @SuppressLint("MissingPermission")
    override suspend fun discoverDevices(): List<BluetoothDevice> {
        val discoveredDevices = mutableListOf<BluetoothDevice>()
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices

        for (device in pairedDevices) {
            if (device.bluetoothClass.deviceClass == ZEBRA_PRINTER_DEVICE_CLASS_ID) {
                discoveredDevices.add(device)
            }
        }
        return discoveredDevices
    }

    override suspend fun connectToDevice(device: BluetoothDevice) {
        try {
            printerConnectionHandler = ZebraPrinterConnectionImpl(device)
            printerConnectionHandler.connect()
            printerConnectionHandler.open()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun isDeviceConnected(): Boolean = printerConnectionHandler.isConnected()
    override fun print(buffer: ByteArray) = flow {
        emit(PrintingState.Printing)
        try {
            printerConnectionHandler.write(buffer)
            delay(5000L)
            printerConnectionHandler.close()
            emit(PrintingState.Done)

        } catch (e: Exception) {
            printerConnectionHandler.close()
            emit(PrintingState.Error)
        }
    }

    override fun getPrinterFactory(): Printer {
        return (printerConnectionHandler as ZebraPrinterConnectionImpl).let {
            val dotsWidthPerRow = 612
            if (it.getLanguage() == PrinterLanguage.ZPL) {
                ZPLPrinter(
                    dotsWidthPerRow,
                    TextSizeConverter.convertSpToDots(2),
                    PRINTER_LABEL_START_X_POSITION,
                    PRINTER_LABEL_START_Y_POSITION
                );
            } else {
                CPCLPrinter(
                    dotsWidthPerRow,
                    TextSizeConverter.convertSpToDots(2),
                    PRINTER_LABEL_START_X_POSITION,
                    PRINTER_LABEL_START_Y_POSITION
                );
            }
        }
    }


    companion object {
        const val DISCOVERY_DEVICES_REGISTER_TIMEOUT = 10000L
        const val ZEBRA_PRINTER_DEVICE_CLASS_ID = 1664
        const val PRINTER_LABEL_START_X_POSITION = 0
        const val PRINTER_LABEL_START_Y_POSITION = 50
    }

}
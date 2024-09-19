package com.lsp.printer.printer

import android.bluetooth.BluetoothSocket
import com.lsp.printer.Bluetooth.BluetoothFactory
import com.lsp.printer.data.local.PrinterLocalDataSource
import com.lsp.printer.data.models.Printer
import java.io.IOException
import javax.inject.Inject

class PrinterFactoryImpl @Inject constructor(
    private val bluetoothFactory: BluetoothFactory,
    private val printerLocalDataSource: PrinterLocalDataSource
): PrinterFactory {
    override fun getDefault(): Printer? = printerLocalDataSource.defaultPrinter

    override suspend fun connect() {
        try {
//            val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
        }catch (e: IOException){

        }
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun print() {
        TODO("Not yet implemented")
    }

    override suspend fun discoverPrinter(): List<Printer> {
        TODO("Not yet implemented")
    }
}
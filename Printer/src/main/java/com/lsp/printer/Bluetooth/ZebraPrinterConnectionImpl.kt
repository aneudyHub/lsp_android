package com.lsp.printer.Bluetooth

import android.bluetooth.BluetoothDevice
import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.PrinterLanguage
import com.zebra.sdk.printer.ZebraPrinter
import com.zebra.sdk.printer.ZebraPrinterFactory

class ZebraPrinterConnectionImpl(private val device: BluetoothDevice) : PrinterConnectionHandler {
    private lateinit var connection: Connection
    override fun connect() {
        connection = BluetoothConnection(device.address)
    }

    override fun isConnected(): Boolean = connection.isConnected

    override fun open() {
        connection.open()
    }

    override fun close() {
        connection.close()
    }

    override fun write(buffer: ByteArray) {
        connection.write(buffer)
    }

    override fun isPrinting(): Boolean = !ZebraPrinterFactory.getInstance(connection).currentStatus.isPaused

    fun getLanguage(): PrinterLanguage {
        val zebraInstance = ZebraPrinterFactory.getInstance(connection)
        return zebraInstance.printerControlLanguage
    }

    fun getLabelLengthInDots(): Int =
        ZebraPrinterFactory.getInstance(connection).currentStatus.labelLengthInDots
}
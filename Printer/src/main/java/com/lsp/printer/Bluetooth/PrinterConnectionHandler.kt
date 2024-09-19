package com.lsp.printer.Bluetooth

import kotlin.jvm.Throws

interface PrinterConnectionHandler {
    @Throws
    fun connect()
    fun isConnected(): Boolean
    fun open()
    fun close()
    fun write(buffer: ByteArray)
    fun isPrinting(): Boolean

}
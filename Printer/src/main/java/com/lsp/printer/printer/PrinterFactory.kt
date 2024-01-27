package com.lsp.printer.printer

import com.lsp.printer.data.models.Printer

interface PrinterFactory {
    fun getDefault(): Printer?
    suspend fun connect()
    suspend fun disconnect()

    suspend fun print()

    suspend fun discoverPrinter(): List<Printer>
}
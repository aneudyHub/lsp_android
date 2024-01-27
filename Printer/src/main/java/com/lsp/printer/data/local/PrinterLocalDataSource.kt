package com.lsp.printer.data.local

import com.lsp.printer.data.models.Printer

interface PrinterLocalDataSource {
    var defaultPrinter: Printer?
}
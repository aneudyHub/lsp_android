package com.lsp.printer.Bluetooth

import com.lsp.printer.printer.Printer

sealed class PrintingState {
    object NotStarted: PrintingState()
    object Printing: PrintingState()
    object Done: PrintingState()
    object Error: PrintingState()
}
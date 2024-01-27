package com.lsp.printer.data.local

import android.content.SharedPreferences
import com.lsp.printer.data.models.Printer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject


class PrinterLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PrinterLocalDataSource {
    override var defaultPrinter: Printer?
        get() = getPrinter()
        set(value) {
            savePrinter(value)
        }


    private fun savePrinter(printer: Printer?) {
        printer?.let {
            with(sharedPreferences.edit()) {
                putInt(PRINTER_ID_KEY, printer.id)
                putString(PRINTER_CUSTOM_NAME_KEY, printer.customName)
                putString(PRINTER_MAC_ADDRESS_KEY, printer.macAddress)
            }
        }
    }

    private fun getPrinter(): Printer {
        return with(sharedPreferences) {
            Printer(
                getInt(PRINTER_ID_KEY, 0),
                getString(PRINTER_CUSTOM_NAME_KEY, "")!!,
                getString(PRINTER_MAC_ADDRESS_KEY, "")!!,
            )
        }
    }

    companion object {
        const val PRINTER_ID_KEY = "printer_id_key"
        const val PRINTER_CUSTOM_NAME_KEY = "printer_custom_name_key"
        const val PRINTER_MAC_ADDRESS_KEY = "printer_mac_address_key"
    }
}
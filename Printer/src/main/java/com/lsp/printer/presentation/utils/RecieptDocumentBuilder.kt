package com.lsp.printer.presentation.utils

import com.lsp.printer.data.models.RecieptData
import com.lsp.printer.printer.Printer
import com.lsp.printer.printer.PrinterTextAlignment
import com.lsp.printer.printer.PrinterTextFormat
import com.lsp.printer.printer.RecieptDetail
import com.lsp.printer.printer.utils.ConcatByteArrays

class RecieptDocumentBuilder private constructor(
    val recieptData: RecieptData,
    val printer: Printer
) {

    private fun generateDetailsTable(details: List<RecieptDetail>): Array<String> {
        return details.map { "${it.description}${printer.tableSeparator}${it.amount}" }
            .toTypedArray()
    }

    private fun generateTotalTable(): Array<String> {
        return arrayOf(
            "TOTAL:${printer.tableSeparator}${recieptData.recieptTotal}",
            "DESCUENTO:${printer.tableSeparator}${recieptData.discount}",
            "TOTAL PAGADO:${printer.tableSeparator}${recieptData.totalPaid}"
        )
    }

    fun getBuffer(): ByteArray {
        val fontSize = 12
        val cmd = ConcatByteArrays.concatByteArrays(
            printer.initialize(),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                recieptData.companyName
            ),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                recieptData.companyAddress
            ),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                "Tel.: ${recieptData.companyPhone}"
            ),
            printer.skipLine(fontSize,4),
            printer.printLine(
                fontSize,
                PrinterTextFormat.BOLD,
                PrinterTextAlignment.CENTER,
                recieptData.type.toString()
            ),
            printer.skipLine(fontSize, 6),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                "Fecha: ${recieptData.recieptDate}"
            ),
            printer.printHorizontalLine('*'),
            printer.skipLine(fontSize, 2),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                "#Prestamo: ${recieptData.loanNumber}"
            ),
            printer.printLine(fontSize, PrinterTextFormat.NORMAL, recieptData.customerName),
            printer.skipLine(fontSize, 2),
            printer.printTable(
                fontSize,
                PrinterTextFormat.NORMAL,
                generateDetailsTable(recieptData.recieptItems)
            ),
//            printer.printHorizontalLine('-'),
            printer.skipLine(fontSize, 2),
            printer.printTable(fontSize, PrinterTextFormat.NORMAL, generateTotalTable()),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                recieptData.userName
            ),
//            printer.printHorizontalLine('-'),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                "LE ATENDIO"
            ),
            printer.printLine(fontSize, PrinterTextFormat.BOLD, "NOTA:"),
            printer.printLine(
                fontSize,
                PrinterTextFormat.NORMAL,
                "ESTO ES PROBANDO DE QUE REALMENTE FUNCIONA"
            ),
            printer.printLine(
                10,
                PrinterTextFormat.NORMAL,
                PrinterTextAlignment.CENTER,
                "***No somos responsable de dinero entregado sin recibo firmado ****"
            ),
            printer.end()
        )

        return cmd
    }

    companion object {
        class Builder() {
            private var recieptData: RecieptData? = null
            private var printer: Printer? = null
            fun recieptData(data: RecieptData): Builder {
                this.recieptData = data
                return this
            }

            fun printer(printer: Printer): Builder {
                this.printer = printer
                return this
            }


            fun build(): RecieptDocumentBuilder {
                return RecieptDocumentBuilder(recieptData!!, printer!!)
            }
        }
    }
}
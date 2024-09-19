package com.lsp.printer.data.models

import com.lsp.printer.presentation.utils.RecieptDocumentType
import com.lsp.printer.printer.RecieptDetail

data class RecieptData(
    val type: RecieptDocumentType,
    val companyName: String,
    val companyAddress: String,
    val companyPhone: String,
    val recieptNumber: String,
    val recieptDate: String,
    val customerName: String,
    val loanNumber: String,
    val recieptTotal: String,
    val discount: String,
    val totalPaid: String,
    val userName: String,
    val recieptItems: List<RecieptDetail>
): PrinterDocumentData()
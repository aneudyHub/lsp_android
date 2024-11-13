package com.system.lsp.data.local.database.entities

import java.sql.Date

data class ExpiredLoanDetail(
    val loanId: Long,
    val customerName: String,
    val customerDocumentId: String,
    val customerAddress: String,
    val customerPhone: String,
    val quota: Int,
    val capital: Double,
    val interest: Double,
    val delayInterest: Double,
    val dueDate: Date?,
    val paidAmount: Double,
    val isPaid: Boolean
)
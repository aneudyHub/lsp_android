package com.system.lsp.domain

import com.system.lsp.data.local.database.dao.LoansDao
import java.sql.Date
import javax.inject.Inject

class GetLoansDueToTodayUseCase @Inject constructor(
    private val loansDao: LoansDao,
) {

    suspend operator fun invoke(): List<LoanSummary> {
        val expiredLoans = loansDao.getExpiredUnpaidLoans()
        return expiredLoans.filter { it.loanId == 4565L }.groupBy { it.loanId }.map { (loanId, details) ->
            val customerName = details.first().customerName
            val totalExpiredQuotas = details.size
            val totalAmountToPay = details.filter { !it.isPaid }.sumOf { (it.capital + it.interest + it.delayInterest ) - it.paidAmount }

            LoanSummary(
                loanId = loanId,
                totalExpiredQuotas = totalExpiredQuotas,
                customerName = customerName,
                customerDocumentId = details.first().customerDocumentId,
                dueDate = details.first().dueDate.toString(),
                customerAddress = details.first().customerAddress,
                customerPhone = details.first().customerPhone,
                totalAmountToPay = totalAmountToPay
                )
        }.sortedBy { it.loanId }
    }

}

data class LoanSummary(
    val loanId: Long,
    val totalExpiredQuotas: Int,
    val customerName: String,
    val customerDocumentId: String,
    val dueDate: String,
    val customerAddress: String,
    val customerPhone: String,
    val totalAmountToPay: Double
)
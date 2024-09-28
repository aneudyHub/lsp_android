package com.system.lsp.domain

import com.system.lsp.data.local.database.dao.LoansDao
import java.sql.Date
import javax.inject.Inject

class GetLoansDueToTodayUseCase @Inject constructor(
    private val loansDao: LoansDao,
) {

    suspend operator fun invoke(): List<LoanSummary> {
        val today = Date(System.currentTimeMillis())
        val expiredLoans = loansDao.getExpiredUnpaidLoans()
        // Agrupar por loanId y cliente, y calcular el total de cuotas vencidas y el total a pagar
        return expiredLoans.groupBy { it.loanId }.map { (loanId, details) ->
            val customerName = details.first().customerName
            val totalExpiredQuotas = details.size
            val totalAmountToPay = details.sumOf { it.capital + it.interest + it.delayInterest }

            LoanSummary(loanId, customerName, totalExpiredQuotas, totalAmountToPay)
        }
    }

}

data class LoanSummary(
    val loanId: Long,
    val customerName: String,
    val totalExpiredQuotas: Int,
    val totalAmountToPay: Double
)
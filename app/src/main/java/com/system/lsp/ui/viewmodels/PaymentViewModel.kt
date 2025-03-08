package com.system.lsp.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.CustomerRepository
import com.system.lsp.ui.viewmodels.exts.calculateDaysFromNow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val loansDao: LoansDao,
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> get() = _uiState

    fun loadLoanData(loanId: Long) {
        viewModelScope.launch {
            val loanWithDetails = loansDao.getLoanWithDetailsById(loanId)
            val paymentDetails = loanWithDetails.details.filter { !it.isPaid!! }.map {
                PaymentDetail(
                    quota = it.quota!!,
                    delayDays = it.dueDate?.calculateDaysFromNow() ?: 0L,
                    quotaDate = it.dueDate.toString(),
                    quotaTotalAmount = (it.capital!! + it.interest!!),
                    quotaDelayInterestAmount = it.delayInterest!!,
                    quotaDelayInterestPaid = it.delayInterestPaid!!,
                    quotaTotalPendingAmount = (it.capital!! + it.interest!! + it.delayInterest!!) - it.paidAmount!!,
                    quotaPaidAmount = it.paidAmount!!
                )
            }

            val customerName = when (val customer =
                customerRepository.getById(loanWithDetails.loan.customerId!!)) {
                is Result.Error -> null
                is Result.Success -> customer.data.name
            }
            val totalDue = paymentDetails.sumOf { it.quotaTotalPendingAmount }
            val totalFine = paymentDetails.sumOf { it.quotaTotalAmount - it.quotaPaidAmount }
            val totalInstallment =
                paymentDetails.sumOf { it.quotaDelayInterestAmount - it.quotaDelayInterestPaid }


            _uiState.update {
                it.copy(
                    loanId = loanId,
                    totalDue = totalDue,
                    totalFine = totalFine,
                    totalInstallment = totalInstallment,
                    totalPending = totalDue + totalFine,
                    clientName = customerName ?: "",
                    paymentDetails = paymentDetails
                )
            }
        }
    }

    fun processPayment(amount: Double) {
        viewModelScope.launch {
            // Simulate payment processing logic
            _uiState.update {
                val remainingDue = it.totalPending - amount
                it.copy(totalPending = remainingDue.coerceAtLeast(0.0))
            }
        }
    }
}


data class PaymentsUiState(
    val totalDue: Double = 0.0,
    val totalFine: Double = 0.0,
    val totalInstallment: Double = 0.0,
    val totalPending: Double = 0.0,
    val clientName: String = "",
    val loanId: Long = 0L,
    val paymentDetails: List<PaymentDetail> = emptyList()
)

data class PaymentDetail(
    val quota: Int,
    val delayDays: Long,
    val quotaDate: String,
    val quotaTotalAmount: Double,
    val quotaDelayInterestAmount: Double,
    val quotaDelayInterestPaid: Double,
    val quotaTotalPendingAmount: Double,
    val quotaPaidAmount: Double
)

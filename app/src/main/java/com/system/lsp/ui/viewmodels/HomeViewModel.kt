package com.system.lsp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.domain.GetLoansDueToTodayUseCase
import com.system.lsp.domain.LoanSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLoansDueToTodayUseCase: GetLoansDueToTodayUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState = _uiState.asStateFlow()

    init {
        getList()
    }

    fun getList() {
        _uiState.value = UiState.OnLoading(true)
        viewModelScope.launch {
            _uiState.value = UiState.OnLoading(false)
            val list = getLoansDueToTodayUseCase()
            _uiState.value = UiState.OnResult(list)
        }
    }

    fun onItemClicked(loanSummary: LoanSummary) {
        _uiState.value = UiState.OnNavToLoan(loanSummary)
        _uiState.value = UiState.Init
    }

    fun onPhotoClicked(documentId: String) {
        _uiState.value = UiState.OnShowDocumentPhoto(documentId)
        _uiState.value = UiState.Init
    }

    fun onPhoneClicked(phoneList: List<String>) {
        _uiState.value = UiState.OnShowPhoneModal(phoneList)
        _uiState.value = UiState.Init
    }

    sealed class UiState {
        object Init : UiState()
        data class OnLoading(val isLoading: Boolean) : UiState()
        data class OnResult(val customersList: List<LoanSummary>) : UiState()
        data class OnShowDocumentPhoto(val documentId: String) : UiState()

        data class OnShowPhoneModal(val phones: List<String>) : UiState()
        data class OnNavToLoan(val loanSummary: LoanSummary): UiState()
    }
}
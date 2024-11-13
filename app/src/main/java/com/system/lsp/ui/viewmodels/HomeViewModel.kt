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

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getList()
    }

    fun getList() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val list = getLoansDueToTodayUseCase()
            _uiState.update { it.copy(isLoading = false, customersList = list) }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val customersList: List<LoanSummary> = arrayListOf()
    )
}
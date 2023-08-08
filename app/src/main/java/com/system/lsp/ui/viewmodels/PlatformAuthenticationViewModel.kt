package com.system.lsp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.domain.AuthenticatePlatformUseCase
import com.system.lsp.domain.UseCaseResult
import com.system.lsp.ui.extensions.getStringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatformAuthenticationViewModel @Inject constructor(
    private val authenticatePlatform: AuthenticatePlatformUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState = _uiState.asStateFlow()


    private fun updateState(uiState: UiState) {
        _uiState.value = uiState
    }

    fun authenticateBtnHasBeenPressed(hashCode: String) {
        updateState(UiState.Loading)
        viewModelScope.launch {
            delay(3000L)
            when (val response = authenticatePlatform(hashCode)) {
                is UseCaseResult.Success -> {
                    updateState(UiState.Success)
                }

                is UseCaseResult.Error -> {
                    updateState(UiState.Error(response.errorType.getStringResource()))
                }
            }
        }

    }

    sealed class UiState {
        object Init : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val error: Int) : UiState()
    }

}
package com.system.lsp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsp.logger.LogFactory
import com.system.lsp.R
import com.system.lsp.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.system.lsp.domain.UseCaseResult
import com.system.lsp.ui.extensions.getStringResource

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val logger: LogFactory
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    private fun updateState(uiState: UiState) {
        _uiState.value = uiState
        logger.logDebug(TAG, "UiState has changed", hashMapOf("uiState" to uiState))
    }


    fun signIn(userName: String, password: String) {
        updateState(UiState.Loading)
        if (userName.isBlank() || password.isBlank()) {
            updateState(UiState.Failed(R.string.user_password_should_be_not_empty))
            return
        }
        viewModelScope.launch {
            when (val response = signInUseCase(userName, password)) {
                is UseCaseResult.Error -> {
                    updateState(UiState.Failed(response.errorType.getStringResource()))
                }

                else -> {
                    updateState(UiState.Success)
                }
            }
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }


    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Failed(val errorResource: Int) : UiState()
    }
}
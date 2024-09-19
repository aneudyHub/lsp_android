package com.system.lsp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.domain.GetCurrentUserUseCase
import com.system.lsp.domain.LoadPlatformDataUseCase
import com.system.lsp.domain.UseCaseResult
import com.system.lsp.ui.extensions.getStringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val loadPlatformDataUseCase: LoadPlatformDataUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState = _uiState.asStateFlow()

    init {
        updateState(UiState.Loading(40))
        viewModelScope.launch {
            delay(3000L)
            updateState(UiState.Loading(100))
            delay(500L)
            when (val platformData = loadPlatformDataUseCase()) {
                is UseCaseResult.Success -> {
                    if (platformData.data == null) {
                        updateState(UiState.UnAuthorized)
                    } else {
                        when (val currentUser = getCurrentUserUseCase()) {
                            is UseCaseResult.Success -> {
                                if (currentUser.data == null) {
                                    updateState(UiState.UserNotSignedIn)
                                } else {
                                    updateState(UiState.AuthorizedAndSignedIn)
                                }
                            }

                            is UseCaseResult.Error -> {
                                updateState(UiState.Error(currentUser.errorType.getStringResource()))
                            }
                        }
                    }
                }

                is UseCaseResult.Error -> {
                    updateState(UiState.Error(platformData.errorType.getStringResource()))
                }
            }
        }
    }

    private fun updateState(uiState: UiState) {
        _uiState.value = uiState
    }

    sealed class UiState {
        object Init : UiState()
        data class Loading(val progress: Int) : UiState()
        object UnAuthorized : UiState()
        object AuthorizedAndSignedIn : UiState()
        object UserNotSignedIn : UiState()
        data class Error(val error: Int) : UiState()
    }

}
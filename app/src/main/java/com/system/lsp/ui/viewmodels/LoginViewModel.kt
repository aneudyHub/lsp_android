package com.system.lsp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.system.lsp.data.remote.models.Result
import com.system.lsp.ui.extensions.getStringResource

@HiltViewModel
class LoginViewModel @Inject constructor(
    val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState = _uiState.asStateFlow()


    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = signInUseCase.execute(userName, password)
            when (response) {
                is Result.Error -> {
                    _uiState.value = UiState.Failed(response.error.getStringResource())
                }

                else -> {
                    _uiState.value = UiState.Success
                }
            }
        }
    }


    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Failed(val errorResource: Int) : UiState()
    }


}
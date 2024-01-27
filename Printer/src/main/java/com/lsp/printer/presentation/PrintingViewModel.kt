package com.lsp.printer.presentation

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsp.printer.Bluetooth.BluetoothFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrintingViewModel @Inject constructor(
    private val bluetoothFactory: BluetoothFactory,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            delay(2000L)
            if(!bluetoothFactory.isSupported){
                _uiState.value = UiState.BluetoothNotSupported
                return@launch
            }

            if (!bluetoothFactory.isEnabled) {
                _uiState.value = UiState.RequestBluetooth(bluetoothFactory.enableActionIntent)
            }
        }
    }

    fun handleRequestBluetoothResult(result: Int) {
        viewModelScope.launch {
            delay(2000L)
            if (result == BLUETOOTH_ENABLED_REQUEST_CODE) {
                _uiState.value = UiState.BluetoothIsEnabled
            }
        }
    }


    sealed class UiState {
        object Init : UiState()
        object BluetoothNotSupported: UiState()

        data class RequestBluetooth(val requestAction: String) : UiState()

        object BluetoothIsEnabled : UiState()

        data class Error(val error: Int) : UiState()

    }

    companion object {
        const val BLUETOOTH_ENABLED_REQUEST_CODE = -1
    }

}
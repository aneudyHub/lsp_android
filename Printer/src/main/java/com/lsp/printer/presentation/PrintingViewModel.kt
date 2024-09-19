package com.lsp.printer.presentation

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.printer.R
import com.lsp.printer.Bluetooth.BluetoothFactory
import com.lsp.printer.Bluetooth.PrintingState
import com.lsp.printer.data.models.PrinterDocumentData
import com.lsp.printer.data.models.RecieptData
import com.lsp.printer.presentation.utils.RecieptDocumentBuilder
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

            delay(3000L)
            if (!bluetoothFactory.isSupported) {
                _uiState.value = UiState.BluetoothNotSupported
                return@launch
            }

            if (!bluetoothFactory.isEnabled) {
                _uiState.value = UiState.RequestBluetooth(bluetoothFactory.enableActionIntent)
                return@launch
            }

            searchPrinter()
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

    private suspend fun searchPrinter() {
        _uiState.value = UiState.SearchingPairedDevices
        val devices = bluetoothFactory.discoverDevices()
        _uiState.value = UiState.PairedDevicesFound(devices)
        println("printers :" + devices)
    }

    fun connectToDevice(device: BluetoothDevice) {
        _uiState.value = UiState.ConnectingToDevice
        viewModelScope.launch {
            bluetoothFactory.connectToDevice(device)
            delay(2000L)
            val isConnected = bluetoothFactory.isDeviceConnected()
            _uiState.value = if (isConnected) {
                UiState.DeviceConnected
            } else {
                UiState.Error(R.string.connection_error)
            }
        }
    }

    fun print(document: PrinterDocumentData) {
        viewModelScope.launch {
            val printer = bluetoothFactory.getPrinterFactory()
            val buffer =
                RecieptDocumentBuilder
                    .Companion
                    .Builder()
                    .recieptData(document as RecieptData)
                    .printer(printer)
                    .build()
                    .getBuffer()
            bluetoothFactory.print(buffer).collect {
                when (it) {
                    PrintingState.Done -> _uiState.value = UiState.PrintingDone
                    PrintingState.Error -> _uiState.value =
                        UiState.Error(R.string.bluetooth_print_failed)

                    PrintingState.NotStarted -> {}
                    PrintingState.Printing -> _uiState.value = UiState.Printing
                }
            }
        }
    }


    sealed class UiState {
        object Init : UiState()
        object BluetoothNotSupported : UiState()

        object SearchingPairedDevices : UiState()

        object DeviceConnected : UiState()

        object ConnectingToDevice : UiState()

        object Printing : UiState()

        object PrintingDone : UiState()

        data class PairedDevicesFound(val devices: List<BluetoothDevice>) : UiState()

        data class RequestBluetooth(val requestAction: String) : UiState()

        object BluetoothIsEnabled : UiState()

        data class Error(val error: Int) : UiState()

    }

    companion object {
        const val BLUETOOTH_ENABLED_REQUEST_CODE = -1
    }

}
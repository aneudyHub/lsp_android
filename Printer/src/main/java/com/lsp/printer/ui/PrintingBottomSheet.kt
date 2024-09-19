package com.lsp.printer.ui

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printer.R
import com.example.printer.databinding.PrintingBottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lsp.printer.data.models.RecieptData
import com.lsp.printer.presentation.PrintingViewModel
import com.lsp.printer.presentation.utils.RecieptDocumentType
import com.lsp.printer.printer.RecieptDetail
import com.lsp.printer.printer.RecieptPrinterDocumentBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class PrintingBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: PrintingViewModel
    private lateinit var binding: PrintingBottomSheetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PrintingBottomSheetFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PrintingViewModel::class.java]
        viewModel.uiState.onEach {
            when (it) {
                is PrintingViewModel.UiState.RequestBluetooth -> {
                    binding.state.text = getString(R.string.requesting_bluetooth)
                    val enableBtIntent = Intent(it.requestAction)
                    startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST_CODE)
                }

                is PrintingViewModel.UiState.Error -> {
                    binding.state.text = getString(it.error)
                    binding.state.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.pairedDevicesRV.isVisible = false
                }

                PrintingViewModel.UiState.Init -> {
                    binding.state.text = getString(R.string.init_bluetooth)
                    binding.pairedDevicesRV.isVisible = false
                }

                PrintingViewModel.UiState.BluetoothIsEnabled -> {
                    binding.state.text = getString(R.string.bluetooth_enabled)
                    binding.pairedDevicesRV.isVisible = false
                }

                PrintingViewModel.UiState.BluetoothNotSupported -> {
                    binding.state.text = getString(R.string.bluetooth_not_supported)
                    binding.pairedDevicesRV.isVisible = false
                }

                PrintingViewModel.UiState.SearchingPairedDevices -> {
                    binding.state.text = getString(R.string.bluetooth_searching_paired_devices)
                    binding.pairedDevicesRV.isVisible = false
                }

                is PrintingViewModel.UiState.PairedDevicesFound -> {
                    binding.state.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.pairedDevicesRV.isVisible = true
                    setPairedDevicesAdapter(it.devices)
                }

                PrintingViewModel.UiState.ConnectingToDevice -> {
                    binding.state.isVisible = true
                    binding.progressBar.isVisible = true
                    binding.pairedDevicesRV.isVisible = false
                    binding.state.text = getString(R.string.connecting_to_device)
                }

                PrintingViewModel.UiState.DeviceConnected -> {
                    binding.pairedDevicesRV.isVisible = false
                    binding.state.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.state.text = getString(R.string.device_connected)
                    print()
                }

                PrintingViewModel.UiState.Printing -> {
                    binding.pairedDevicesRV.isVisible = false
                    binding.state.isVisible = true
                    binding.progressBar.isVisible = true
                    binding.state.text = getString(R.string.bluetooth_printing)
                }
                PrintingViewModel.UiState.PrintingDone -> {
                    dismiss()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setPairedDevicesAdapter(devices: List<BluetoothDevice>) {
        binding.pairedDevicesRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.pairedDevicesRV.adapter = PairedDevicesAdapter(devices) {
            viewModel.connectToDevice(it)
        }
    }

    private fun print() {
//
        val document = arguments?.getSerializable(DOCUMENT_SERIALIZABLE) as RecieptData
        viewModel.print(document)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
            viewModel.handleRequestBluetoothResult(resultCode)
        }
    }

    companion object {
        const val BLUETOOTH_REQUEST_CODE = 1000
        const val DOCUMENT_SERIALIZABLE = "document"
    }
}
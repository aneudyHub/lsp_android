package com.lsp.printer.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.printer.R
import com.example.printer.databinding.PrintingBottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lsp.printer.presentation.PrintingViewModel
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
            when(it){
                is PrintingViewModel.UiState.RequestBluetooth -> {
                    binding.state.text = getString(R.string.requesting_bluetooth)
                    val enableBtIntent = Intent(it.requestAction)
                    startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST_CODE)
                }
                is PrintingViewModel.UiState.Error -> TODO()
                PrintingViewModel.UiState.Init -> {
                    binding.state.text = getString(R.string.init_bluetooth)
                }

                PrintingViewModel.UiState.BluetoothIsEnabled -> {
                    binding.state.text = getString(R.string.bluetooth_enabled)
                }

                PrintingViewModel.UiState.BluetoothNotSupported -> {
                    binding.state.text = getString(R.string.bluetooth_not_supported)
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == BLUETOOTH_REQUEST_CODE){
            viewModel.handleRequestBluetoothResult(resultCode)
        }
    }

    companion object{
        const val BLUETOOTH_REQUEST_CODE = 1000
    }
}
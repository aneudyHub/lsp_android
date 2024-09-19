package com.lsp.printer.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.printer.R
import com.example.printer.databinding.PairedDeviceItemBinding

class PairedDevicesAdapter(private val devices: List<BluetoothDevice>, private val onDeviceClicked: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<PairedDevicesAdapter.ViewHolder>() {

    class ViewHolder(val binding: PairedDeviceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PairedDeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.binding.printerName.text = device.name
        holder.binding.root.setOnClickListener { onDeviceClicked(device) }
    }

    override fun getItemCount(): Int = devices.size
}
package com.lsp.printer

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.example.printer.R
import com.lsp.printer.ui.PrintingBottomSheet

class PrinterActivity : AppCompatActivity() {

//    private val bluetoothAdapter: BluetoothAdapter? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val bluetoothManager = getSystemService(BluetoothManager::class.java)
//        bluetoothManager.adapter
//    } else {
//        BluetoothAdapter.getDefaultAdapter()
//    }

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)

        val button = findViewById<Button>(R.id.blubutton)
        button.setOnClickListener{
            enableBT()
        }

        val buscarBTN = findViewById<Button>(R.id.button2)
        buscarBTN.setOnClickListener {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
        }

        val dialogBtn = findViewById<Button>(R.id.button3)
        dialogBtn.setOnClickListener {
            val bottomSheetFragment = PrintingBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("BT","onReceive ${intent.action}")
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address

                    Log.e("BT","devices ${deviceName} ${deviceHardwareAddress}")
                }
            }
        }
    }

    private fun pairedDevices(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("BT","PERmission denied buscarBTN")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address

            Log.e("BT","devices ${deviceName} ${deviceHardwareAddress}")
        }
    }

    private fun enableBT(){
        if(!bluetoothAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("BT","PERmission denied")
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    100
                )
                return
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_ENABLE_BT ->{
                Log.e("BT","RESULT ${resultCode} ${data}")
            }
        }
    }

    companion object{
        private val REQUEST_ENABLE_BT = 100
    }


}
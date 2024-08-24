package com.system.lsp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BluetoothPermissionHelper {

    // Request code for Bluetooth permissions
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 1001;

    // Method to check and request Bluetooth permissions
    public static void requestBluetoothPermissions(Activity activity) {
        // Check if Bluetooth permissions are granted
        if (
                ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are already granted
            // You can proceed with Bluetooth operations here
        } else {
            // Permissions are not granted, request them
            requestPermission(activity,Manifest.permission.BLUETOOTH);
            requestPermission(activity,Manifest.permission.BLUETOOTH_ADMIN);
            requestPermission(activity,Manifest.permission.BLUETOOTH_CONNECT);
        }
    }

    private static void requestPermission(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        permission
                },
                BLUETOOTH_PERMISSION_REQUEST_CODE);
    }

    // Method to handle permission request results
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            // Check if permissions were granted
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted
                // You can proceed with Bluetooth operations here
            } else {
                // Permissions were denied
                // Handle the case where the user denies Bluetooth permissions
            }
        }
    }

    public static Boolean isBluetoothConnectGranted(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}

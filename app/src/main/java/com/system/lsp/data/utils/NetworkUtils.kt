package com.system.lsp.data.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

fun getDeviceId(context: Context): String {
    val deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    } else {
        val mTelephony =
            context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ""
            }
        }
        if (mTelephony.deviceId != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mTelephony.imei
            } else {
                mTelephony.deviceId
            }
        } else {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }
    }
    return deviceId
}
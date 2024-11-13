package com.system.lsp

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.system.lsp.data.remote.network.LogoutCallback
import com.system.lsp.sync.DataSyncWorkerFactory
import com.system.lsp.ui.activities.LoginActivity
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LspApp : Application(), Configuration.Provider, LogoutCallback {

    @Inject
    lateinit var workerFactory: DataSyncWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Needed to start Activity from Application context
        startActivity(intent)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}
package com.system.lsp.ui.activities


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.lsp.logger.LogFactory
import com.system.lsp.databinding.ActivitySplashBinding
import com.system.lsp.ui.Main.MainActivity
import com.system.lsp.ui.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var loggerFactory: LogFactory

    override fun onStart() {
        super.onStart()
        loggerFactory.logInfo(TAG, "onStart() has been called")
        viewModel.uiState.onEach {
            loggerFactory.logDebug(TAG, "uiState has been changed", mapOf("state" to it))
            when (it) {
                SplashViewModel.UiState.Init -> {

                }

                SplashViewModel.UiState.AuthorizedAndSignedIn -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is SplashViewModel.UiState.Loading -> {
                    binding.loadingBar.progress = it.progress
                }

                SplashViewModel.UiState.UnAuthorized -> {
                    startActivity(Intent(this, PlatformAuthenticationActivity::class.java))
                    finish()
                }

                SplashViewModel.UiState.UserNotSignedIn -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                is SplashViewModel.UiState.Error -> {

                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        loggerFactory.logInfo(TAG, "onStop() has been called")
        lifecycleScope.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loggerFactory.logDebug(TAG, "onCreated has been called")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }
}
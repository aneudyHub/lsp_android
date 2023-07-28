package com.system.lsp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.lsp.logger.LogFactory
import com.system.lsp.databinding.ActivityPlatformAuthenticationBinding
import com.system.lsp.ui.viewmodels.PlatformAuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PlatformAuthenticationActivity : BaseActivity() {

    @Inject
    lateinit var loggerFactory: LogFactory

    private lateinit var binding: ActivityPlatformAuthenticationBinding
    private val viewModel: PlatformAuthenticationViewModel by viewModels()

    override fun onBackPressed() {

    }

    override fun onStart() {
        super.onStart()
        loggerFactory.logInfo(TAG, "onStart() has been called")

        with(binding) {
            authenticateBtn.setOnClickListener {
                val firstHashText = firstHash.text.toString()
                val secondHashText = secondHash.text.toString()
                val hashCode = "${firstHashText}-${secondHashText}"
                viewModel.authenticateBtnHasBeenPressed(hashCode)
            }
        }

        viewModel.uiState.onEach {
            loggerFactory.logInfo(TAG, "uiState has been changed ", mapOf("uiState" to it))
            when (it) {
                PlatformAuthenticationViewModel.UiState.Init -> {
                    binding.loading.isVisible = false
                    binding.errorMsgPlatform.isVisible = false
                    binding.authenticateBtn.isEnabled = true
                    binding.firstHash.isEnabled = true
                    binding.secondHash.isEnabled = true
                }

                PlatformAuthenticationViewModel.UiState.Loading -> {
                    binding.loading.isVisible = true
                    binding.errorMsgPlatform.isVisible = false
                    binding.authenticateBtn.isEnabled = false
                    binding.firstHash.isEnabled = false
                    binding.secondHash.isEnabled = false
                }

                PlatformAuthenticationViewModel.UiState.Success -> {
                    binding.errorMsgPlatform.isVisible = false
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                is PlatformAuthenticationViewModel.UiState.Error -> {
                    binding.loading.isVisible = false
                    binding.errorMsgPlatform.isVisible = true
                    binding.authenticateBtn.isEnabled = true
                    binding.firstHash.isEnabled = true
                    binding.secondHash.isEnabled = true
                    binding.errorMsgPlatform.text = getString(it.error)
                }
            }
        }.launchIn(lifecycleScope)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loggerFactory.logDebug(TAG, "onCreated has been called")
        binding = ActivityPlatformAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()
        loggerFactory.logInfo(TAG, "onStop() has been called")
        lifecycleScope.cancel()
    }


    companion object {
        private val TAG = PlatformAuthenticationActivity::class.java.simpleName
    }
}
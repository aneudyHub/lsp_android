package com.system.lsp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.lsp.logger.LogFactory
import com.system.lsp.databinding.ActivityLoginBinding
import com.system.lsp.ui.Main.MainActivity
import com.system.lsp.ui.extensions.hideKeyboard
import com.system.lsp.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by Aneudy Vargas on 15/06/2023.
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var loggerFactory: LogFactory
    private val viewModel: LoginViewModel by viewModels()


    override fun onBackPressed() = Unit

    override fun onStart() {
        super.onStart()
        loggerFactory.logInfo(TAG, "onStart() has been called")
        with(binding) {
            loginBtn.setOnClickListener {
                viewModel.signIn(binding.userName.text.toString(), binding.password.text.toString())
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loggerFactory.logInfo(TAG, "onCreate() has been called")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.uiState.onEach {
            when (it) {
                LoginViewModel.UiState.Initial -> {
                    hideProgressBar()
                }

                LoginViewModel.UiState.Success -> {
                    hideProgressBar()
                    val intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                }

                LoginViewModel.UiState.Loading -> {
                    hideKeyboard()
                    showDialog()
                }

                is LoginViewModel.UiState.Failed -> {
                    hideProgressBar()
                    val errorMsg = getString(it.errorResource)
                    Toast.makeText(
                        applicationContext,
                        errorMsg, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showDialog() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loadingBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        loggerFactory.logInfo(TAG, "onStop() has been called")
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}
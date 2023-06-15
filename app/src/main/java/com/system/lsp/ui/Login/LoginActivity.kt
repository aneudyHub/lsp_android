package com.system.lsp.ui.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.system.lsp.databinding.ActivityLoginBinding
import com.system.lsp.ui.Main.MainActivity
import com.system.lsp.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by Aneudy Vargas on 15/06/2023.
 */
@AndroidEntryPoint
class LoginActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        with(binding){
            loginBtn.setOnClickListener {
                viewModel.signIn(binding.userName.text.toString(),binding.password.text.toString())
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.uiState.onEach {
            when(it){
                LoginViewModel.UiState.Initial->{
                    hideDialog()
                }
                LoginViewModel.UiState.Success->{
                    hideDialog()
                    val intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                }
                LoginViewModel.UiState.Loading->{
                    showDialog()
                }
                is LoginViewModel.UiState.Failed->{
                    hideDialog()
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

    private fun hideDialog() {
        binding.loadingBar.visibility = View.GONE
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}
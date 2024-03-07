package com.system.lsp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.data.repositories.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
): ViewModel() {

    fun getList(){
        viewModelScope.launch {
            val list = customerRepository.getAll()
            Log.e("HomeViewModel", list.toString())
        }
    }
}
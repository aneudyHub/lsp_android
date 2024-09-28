package com.system.lsp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.lsp.data.repositories.CustomerRepository
import com.system.lsp.domain.GetLoansDueToTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val getLoansDueToTodayUseCase: GetLoansDueToTodayUseCase
): ViewModel() {

    fun getList(){
        viewModelScope.launch {
//            val list = customerRepository.getAll()
            val list = getLoansDueToTodayUseCase()
            Log.e("HomeViewModel data ", list.toString())
        }
    }
}
package com.system.lsp.ui.fragments

import PaymentDetailsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.system.lsp.databinding.FragmentPaymentsBinding
import com.system.lsp.ui.viewmodels.PaymentDetail
import com.system.lsp.ui.viewmodels.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PaymentsFragment : Fragment() {

    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadLoanData(args.loanId)
    }

    private fun setupRecyclerView(paymentDetails: List<PaymentDetail>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PaymentDetailsAdapter()
        binding.recyclerView.adapter = adapter
        adapter.updateList(paymentDetails)
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                binding.totalFine.text = uiState.totalFine.toString()
                binding.totalInstallment.text = uiState.totalInstallment.toString()
                binding.totalPending.text = uiState.totalPending.toString()
                setupRecyclerView(uiState.paymentDetails)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

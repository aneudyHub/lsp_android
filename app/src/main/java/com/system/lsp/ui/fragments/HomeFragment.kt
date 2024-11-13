package com.system.lsp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.system.lsp.R
import com.system.lsp.databinding.FragmentListaCoutaBinding
import com.system.lsp.domain.LoanSummary
import com.system.lsp.modelo.DatosCliente
import com.system.lsp.ui.adapters.HomeCustomerAdapter
import com.system.lsp.ui.adapters.HomeCustomerAdapter.OnItemClickListener
import com.system.lsp.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewBinding: FragmentListaCoutaBinding by lazy {
        FragmentListaCoutaBinding.inflate(layoutInflater)
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeCustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                viewBinding.refreshLayout.isRefreshing = it.isLoading


                viewBinding.infoData.isVisible = it.customersList.isEmpty()
                viewBinding.reciclador.isVisible = it.customersList.isNotEmpty()

                setAdapter(it.customersList)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        with(viewBinding) {
            reciclador.layoutManager = LinearLayoutManager(requireContext());
            refreshLayout.setOnRefreshListener {
                viewModel.getList()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.searchMain)
        val searchView = searchItem.actionView as SearchView?
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setAdapter(list: List<LoanSummary>) {
        adapter = HomeCustomerAdapter(
            object : OnItemClickListener {
                override fun onClick(loanSummary: LoanSummary) {
                    TODO("Not yet implemented")
                }

                override fun showDocumentPhoto(document: String) {
                    TODO("Not yet implemented")
                }

                override fun showPhoneBottomSheet(phoneList: List<String>) {
                    TODO("Not yet implemented")
                }
            },
            list
        );
        viewBinding.reciclador.adapter = adapter
    }

}
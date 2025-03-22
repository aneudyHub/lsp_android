package com.system.lsp.ui.fragments

//import PaymentsFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.system.lsp.R
import com.system.lsp.databinding.FragmentListaCoutaBinding
import com.system.lsp.domain.LoanSummary
import com.system.lsp.provider.Contract
import com.system.lsp.ui.adapters.HomeCustomerAdapter
import com.system.lsp.ui.adapters.HomeCustomerAdapter.OnItemClickListener
import com.system.lsp.ui.adapters.PhoneNumbersAdapter
import com.system.lsp.ui.viewmodels.HomeViewModel
import com.system.lsp.utilidades.URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest {
                when (it) {
                    HomeViewModel.UiState.Init -> {}
                    is HomeViewModel.UiState.OnLoading -> viewBinding.refreshLayout.isRefreshing =
                        it.isLoading

                    is HomeViewModel.UiState.OnResult -> {
                        viewBinding.infoData.isVisible = it.customersList.isEmpty()
                        viewBinding.reciclador.isVisible = it.customersList.isNotEmpty()
                        setAdapter(it.customersList)
                    }

                    is HomeViewModel.UiState.OnShowDocumentPhoto -> showPhoto(it.documentId)
                    is HomeViewModel.UiState.OnShowPhoneModal -> showPhoneNumberBottomSheet(it.phones)
                    is HomeViewModel.UiState.OnNavToLoan -> goToPaymentActivity(it.loanSummary)
                }
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
                    viewModel.onItemClicked(loanSummary)
                }

                override fun showDocumentPhoto(document: String) {
                    viewModel.onPhotoClicked(document)
                }

                override fun showPhoneBottomSheet(phoneList: List<String>) {
                    viewModel.onPhoneClicked(phoneList)
                }
            },
            list
        );
        viewBinding.reciclador.adapter = adapter
    }

    fun showPhoto(documentId: String) {
        val pDialog: AlertDialog
        val builder = AlertDialog.Builder(
            requireContext()
        )
        builder.setTitle("Foto")
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_foto_layout, null)
        builder.setView(view)
        val photo = view.findViewById<View>(R.id.DialogFoto_Foto) as ImageView

        val options = RequestOptions()
            .fitCenter()
            .placeholder(resources.getDrawable(R.drawable.index))
            .error(resources.getDrawable(R.drawable.index))

        val Url = URL.FOTO + documentId + ".jpg"
        Glide.with(this).load(Url).apply(options).into(photo)
        pDialog = builder.create()
        pDialog.show()
    }

    private fun showPhoneNumberBottomSheet(phoneNumbers: List<String>) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.phone_number_bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PhoneNumbersAdapter(phoneNumbers) { phoneNumber ->
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun goToPaymentActivity(loanSummary: LoanSummary) {
        Log.e("ANEUDY","HERE ENTROOOOOO")
        val action = HomeFragmentDirections.actionMenuItemHomeToPaymentsFragment(loanSummary.loanId)
        findNavController().navigate(action)
    }
}
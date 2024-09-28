package com.system.lsp.ui.activities


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.lsp.logger.LogFactory
import com.lsp.printer.PrinterActivity
import com.lsp.printer.data.models.RecieptData
import com.lsp.printer.presentation.utils.RecieptDocumentType
import com.lsp.printer.printer.RecieptDetail
import com.lsp.printer.ui.PrintingBottomSheet
import com.lsp.printer.ui.PrintingBottomSheet.Companion.DOCUMENT_SERIALIZABLE
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
                    startActivity(Intent(this, DrawerActivity::class.java))
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


//        val details: MutableList<RecieptDetail> = ArrayList()
//
//        details.add(RecieptDetail("Pago Cuota(S) N.18/46", 140.00))
//        details.add(RecieptDetail("Pago Cuota(S) N.18/46", 140.00))
//        details.add(RecieptDetail("Pago Cuota(S) N.18/46", 140.00))
//        details.add(RecieptDetail("Pago Cuota(S) N.18/46", 140.00))
//
//        val document = RecieptData(
//            companyName = "Prestamos Hermanos Hdez",
//            companyAddress = "SFM",
//            companyPhone = "809000000",
//            recieptNumber = "0",
//            recieptDate = "2024-05-28 10:49:02",
//            recieptItems = details,
//            customerName = "Jose Perez",
//            loanNumber = "1234567",
//            recieptTotal = "1000.00",
//            discount = "0.00",
//            totalPaid = "1000.00",
//            userName = "cobrador",
//            type = RecieptDocumentType.ORIGINAL_DOCUMENT
//        )
//
//        val bundle = Bundle()
//        bundle.putSerializable(DOCUMENT_SERIALIZABLE, document)
//
//        val bottomSheetFragment = PrintingBottomSheet()
//        bottomSheetFragment.arguments = bundle
//        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }
}
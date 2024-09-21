package com.system.lsp.sync

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import android.widget.HeaderViewListAdapter
import com.system.lsp.data.remote.models.PagoSyncBody
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.PaymentsRepository
import com.system.lsp.data.repositories.SyncDataRepository
import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.provider.OperacionesBaseDatos
import com.system.lsp.provider.ProcesadorRemoto
import com.system.lsp.utilidades.Resolve
import com.system.lsp.utilidades.UPreferencias
import com.system.lsp.utilidades.URL
import com.system.lsp.web.RESTService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class RemoteSyncHandler @Inject constructor(
    private val syncDataRepository: SyncDataRepository,
    private var paymentsRepository: PaymentsRepository,
    private val usersRepository: UsersRepository
) : SyncHandler() {

    private val TAG = RemoteSyncHandler::class.java.simpleName
    override suspend fun sendRequest() {
        val currentUser = usersRepository.currentUser?: return
        val paymentsResponse = paymentsRepository.getNoSyncedPayments(currentUser.id)
        val requestBody = when(paymentsResponse){
            is Result.Error -> TODO()
            is Result.Success -> {
                val insertions = paymentsResponse.data.map { paymentWithDetails ->
                    PagoSyncBody(
                        userId = paymentWithDetails.paymentEntity?.userId!!,
                        date = paymentWithDetails.paymentEntity.date.toString(),
                        loanId = paymentWithDetails.paymentEntity.loanId?.toInt()!!,
                        amount = paymentWithDetails.paymentDetailsEntity?.sumOf { (it.capital + it.interest + it.delayInterest) }?: 0.0, // TODO: sumarize the total here
                        note = ""
                    )
                }
            }
        }


//        val datos = procesadorRemoto.crearPayload(contentResolver)
//        if (datos == null) {
//            listener.onSuccess()
//            return
//        }
//        Log.d(TAG, "Payload de para subir: $datos")
//
//        val cabeceras = HashMap<String, String>()
//        cabeceras["Authorization"] = UPreferencias.obtenerClaveApi(context)
//        val syncTime = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(context))
//        cabeceras["sync_time"] = syncTime
//
//        RESTService(this.context).post(
//            URL.SERVER + URL.SYNC, datos,
//            { handleResponse(null) },
//            { handleErrors(it) },
//            cabeceras
//        )
    }

//    override fun handleResponse(response: JSONObject?) {
//        procesadorRemoto.desmarcarContactos(contentResolver)
////        listener.onSuccess()
//    }

    override suspend fun run() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                syncDataRepository.retrieveData()
            } catch (e: Exception) {
                Log.e("Error", "Error sending data", e)
            }
        }
        sendRequest()
    }
}
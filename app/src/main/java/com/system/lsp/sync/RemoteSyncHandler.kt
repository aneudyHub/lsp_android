package com.system.lsp.sync

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import com.system.lsp.data.repositories.SyncDataRepository
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
    private val contentResolver: ContentResolver,
    private val context: Context
) : SyncHandler(context) {

    private val TAG = RemoteSyncHandler::class.java.simpleName
    private val procesadorRemoto = ProcesadorRemoto()
    private val operacionesBaseDatos = OperacionesBaseDatos.obtenerInstancia(context)

    @Override
    override fun setListener(listener: SyncHandlerListener) {
        this.listener = listener
    }

    override fun sendRequest() {
        val datos = procesadorRemoto.crearPayload(contentResolver)
        if (datos == null) {
            listener.onSuccess()
            return
        }
        Log.d(TAG, "Payload de para subir: $datos")

        val cabeceras = HashMap<String, String>()
        cabeceras["Authorization"] = UPreferencias.obtenerClaveApi(context)
        val syncTime = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(context))
        cabeceras["sync_time"] = syncTime

        RESTService(this.context).post(
            URL.SERVER + URL.SYNC, datos,
            { handleResponse(null) },
            { handleErrors(it) },
            cabeceras
        )
    }

    override fun handleResponse(response: JSONObject?) {
        procesadorRemoto.desmarcarContactos(contentResolver)
        listener.onSuccess()
    }

    override fun run() {
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
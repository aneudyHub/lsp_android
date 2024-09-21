package com.system.lsp.sync

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.content.OperationApplicationException
import android.os.RemoteException
import android.util.Log
import com.system.lsp.provider.Contract
import com.system.lsp.provider.OperacionesBaseDatos
import com.system.lsp.provider.ProcesadorLocal
import com.system.lsp.utilidades.UPreferencias
import com.system.lsp.utilidades.URL
import com.system.lsp.utilidades.UTiempo
import com.system.lsp.web.RESTService
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class LocalSyncHandler(context: Context) : SyncHandler(context) {

    private val TAG = LocalSyncHandler::class.java.simpleName
    private val RESPONSE_CUSTOMERS_ARRAY_KEY = "clientes"
    private val RESPONSE_LOANS_ARRAY_KEY = "prestamos"
    private val RESPONSE_LOANS_DETAILS_ARRAY_KEY = "prestamos_detalles"
    private val RESPONSE_PAYMENTS_ARRAY_KEY = "cuotas_pagas"
    private val contentResolver: ContentResolver = context.contentResolver
    private val operacionesBaseDatos: OperacionesBaseDatos = OperacionesBaseDatos.obtenerInstancia(context)

    override fun run() {
        sendRequest()
    }

    override fun sendRequest() {
        val syncTime = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(this.context))

        val cabeceras = HashMap<String, String>()
        cabeceras["Authorization"] = UPreferencias.obtenerClaveApi(this.context)
        cabeceras["sync_time"] = syncTime

        RESTService(context).get(
                URL.SERVER + URL.SYNC,
                { response -> handleResponse(response) },
                { error -> handleErrors(error) },
                cabeceras
        )
    }

    override fun handleResponse(response: JSONObject) {
        try {
            val ops = ArrayList<ContentProviderOperation>()

            Log.e("response", response.toString())

            val manejadorContactos = ProcesadorLocal()
            manejadorContactos.procesar(
                    response.getJSONArray(RESPONSE_CUSTOMERS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_LOANS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_LOANS_DETAILS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_PAYMENTS_ARRAY_KEY)
            )
            manejadorContactos.procesarOperaciones_Clientes(ops, contentResolver)
            manejadorContactos.procesarOperaciones_Prestamos(ops, contentResolver)
            manejadorContactos.procesarOperaciones_Prestamos_Detalle(ops, contentResolver)

            if (ops.isNotEmpty()) {
                Log.d(TAG, "# Cambios en 'contacto': ${ops.size} ops.")
                contentResolver.applyBatch(Contract.AUTORIDAD, ops)
                contentResolver.notifyChange(Contract.URI_CONTENIDO_BASE, null, false)
            }

            operacionesBaseDatos.actualizarSyncTime(
                    UPreferencias.obtenerIdUsuario(this.context),
                    UTiempo.obtenerFechaHora()
            )
            listener.onSuccess()
        } catch (e: RemoteException) {
            e.printStackTrace()
            listener.onFailure("Error inesperado")
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
            listener.onFailure("Error inesperado")
        } catch (e: JSONException) {
            e.printStackTrace()
            listener.onFailure("Error inesperado")
        }
    }
}
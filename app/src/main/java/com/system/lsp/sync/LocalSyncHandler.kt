package com.system.lsp.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.provider.ProcesadorLocal;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.utilidades.UTiempo;
import com.system.lsp.web.RESTService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalSyncHandler extends SyncHandler {
    private static final String TAG = LocalSyncHandler.class.getSimpleName();
    private static final String RESPONSE_CUSTOMERS_ARRAY_KEY = "clientes";
    private static final String RESPONSE_LOANS_ARRAY_KEY = "prestamos";
    private static final String RESPONSE_LOANS_DETAILS_ARRAY_KEY = "prestamos_detalles";
    private static final String RESPONSE_PAYMENTS_ARRAY_KEY = "cuotas_pagas";
    private ContentResolver contentResolver;
    private OperacionesBaseDatos operacionesBaseDatos;


    public LocalSyncHandler(
            Context context,
            SyncHandlerListener listener
    ) {
        super(context, listener);
        this.contentResolver = context.getContentResolver();
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(context);
    }

    @Override
    public void run(){
        sendRequest();
    }

    @Override
    protected void sendRequest() {
        String syncTime= operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(this.context));

        HashMap<String, String> cabeceras = new HashMap<>();
        cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(this.context));
        cabeceras.put("sync_time",syncTime);

        // Procesar GET
        new RESTService(context).get(URL.SERVER + URL.SYNC,
                (Response.Listener<JSONObject>) this::handleResponse,
                this::handleErrors,
                cabeceras);
    }

    @Override
    protected void handleResponse(JSONObject response) {
        try {
            // Crear referencia de lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            Log.e("response", response.toString());

            // Convertir array JSON de descuentos a modelo
            ProcesadorLocal manejadorContactos = new ProcesadorLocal();
            manejadorContactos.procesar(
                    response.getJSONArray(RESPONSE_CUSTOMERS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_LOANS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_LOANS_DETAILS_ARRAY_KEY),
                    response.getJSONArray(RESPONSE_PAYMENTS_ARRAY_KEY)
            );
            manejadorContactos.procesarOperaciones_Clientes(ops, contentResolver);
            manejadorContactos.procesarOperaciones_Prestamos(ops, contentResolver);
            manejadorContactos.procesarOperaciones_Prestamos_Detalle(ops, contentResolver);

            // ¿ Hay operaciones por realizar ?
            if (!ops.isEmpty()) {
                Log.d(TAG, "# Cambios en \'contacto\': " + ops.size() + " ops.");
                // Aplicar batch de operaciones
                contentResolver.applyBatch(Contract.AUTORIDAD, ops);
                // Notificar cambio al content provider
                contentResolver.notifyChange(Contract.URI_CONTENIDO_BASE, null, false);
            }

            operacionesBaseDatos.actualizarSyncTime(UPreferencias.obtenerIdUsuario(this.context), UTiempo.obtenerFechaHora());
            this.listener.onSuccess();
        } catch (RemoteException | OperationApplicationException | JSONException e) {
            e.printStackTrace();
            this.listener.onFailure("Error inesperado");
        }
    }

}

package com.system.lsp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.provider.ProcesadorRemoto;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.web.RESTService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RemoteSyncHandler extends SyncHandler{
    private static final String TAG = RemoteSyncHandler.class.getSimpleName();
    private ProcesadorRemoto procesadorRemoto;
    private ContentResolver contentResolver;
    private OperacionesBaseDatos operacionesBaseDatos;

    public RemoteSyncHandler(Context context, SyncHandlerListener listener) {
        super(context, listener);
        procesadorRemoto = new ProcesadorRemoto();
        contentResolver = context.getContentResolver();
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(context);
    }

    @Override
    protected void sendRequest() {
        String datos = procesadorRemoto.crearPayload(contentResolver);
        if (datos == null) {
            listener.onSuccess();
            return;
        }
        Log.d(TAG, "Payload de para subir:" + datos);

        HashMap<String, String> cabeceras = new HashMap<>();
        cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(context));
        String syncTime = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(context));
        cabeceras.put("sync_time", syncTime);

        new RESTService(this.context).post(URL.SERVER + URL.SYNC, datos,
                response -> {
                    handleResponse(null);
                },
                this::handleErrors
                , cabeceras);
    }

    @Override
    protected void handleResponse(JSONObject response) {
        procesadorRemoto.desmarcarContactos(contentResolver);
        listener.onSuccess();
    }

    @Override
    public void run() {
        sendRequest();
    }
}

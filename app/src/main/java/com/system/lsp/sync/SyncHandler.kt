package com.system.lsp.sync;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONObject;

abstract public class SyncHandler {

    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;
    private String TAG = SyncHandler.class.getSimpleName();

    protected Context context;
    protected SyncHandlerListener listener;

    protected void setListener(SyncHandlerListener listener) {
        this.listener = listener;
    }

    public SyncHandler(Context context){
        this.context = context;
    }

    protected abstract void sendRequest();
    protected abstract void handleResponse(JSONObject response);

    public abstract void run();

    protected void handleErrors(VolleyError error){
        RespuestaApi respuesta = new RespuestaApi(ESTADO_PETICION_FALLIDA,
                "Petición incorrecta");
        NetworkResponse response = error.networkResponse;
        String json = null;

        // Verificación: ¿La respuesta tiene contenido interpretable?
        if (error.networkResponse != null) {

            String s = new String(error.networkResponse.data);
            try {
                Gson gson = new Gson();
                respuesta = gson.fromJson(s, RespuestaApi.class);
            } catch (JsonSyntaxException e) {
                Log.d(TAG, "Error de parsing: " + s);
            }

        }

        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                case 501:
                case 405:
                    json = new String(response.data);
                    json = Resolve.getValueFromJsonByKey(json, "message");
                    if (json != null) //displayMessage(json);
                        respuesta = new RespuestaApi(response.statusCode
                                , json);
                    break;
                case 401:
                    json = new String(response.data);
                    json = Resolve.getValueFromJsonByKey(json, "message");
                    if (json != null) //displayMessage(json);
                        respuesta = new RespuestaApi(response.statusCode
                                , "No hay coincidencias del token");

                    Resolve.logoutUser(this.context);
                    break;
                case 500:
                    json = new String(response.data);
                    json = Resolve.getValueFromJsonByKey(json, "message");
                    respuesta = new RespuestaApi(response.statusCode
                            , json);
                    break;

            }
        }


        if (error instanceof NetworkError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA
                    , "Error en la conexión. Intentalo de nuevo");
        }

        // Error de espera al servidor
        if (error instanceof TimeoutError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA, "Error de espera del servidor");
        }

        // Error de parsing
        if (error instanceof ParseError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_PARSING, "La respuesta no es formato JSON");
        }

        if (error instanceof NoConnectionError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR
                    , "Servidor no disponible, prueba mas tarde");
        }

        Log.d(TAG, "Error Respuesta:" + (respuesta != null ? respuesta.toString() : "()")
                + "\nDetalles:" + error.getMessage());

        listener.onFailure(respuesta.getMensaje());
//        Resolve.enviarBroadcast(this.context, false, respuesta.getMensaje());
    }
}

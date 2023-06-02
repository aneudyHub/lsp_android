package com.system.lsp.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.DatabaseHandler;
import com.system.lsp.provider.ProcesadorLocal;
import com.system.lsp.provider.ProcesadorRemoto;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.ui.Login.LoginActivity;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.web.RESTService;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suarez on 12/02/2018.
 */

public class HistorialPagos {


    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;
    private static final String TAG = HistorialPagos.class.getSimpleName();

    private Context context;

    private DatabaseHandler db;
    private ContentResolver cr;
    private Gson gson = new Gson();
    private ProcesadorRemoto procRemoto = new ProcesadorRemoto();

    public HistorialPagos(Context context) {
        this.context = context;
        cr = context.getContentResolver();
    }

    public void synCuotaPagaLocal(String fecha) {
        // Construcción de cabeceras
        Log.e("sync local","sincronizando--------------");
        HashMap<String, String> cabeceras = new HashMap<>();
        cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(context));
        cabeceras.put("sync_time", "0");
        Log.e("ESTADO 107","1");

        boolean t = Resolve.isInternetAvailable();
        Log.e("verifica red==>",String.valueOf(t));
        //if(t){
        new RESTService(context).get(UPreferencias.obtenerUrlAPP(context)+URL.HISTORIAL+"?fecha="+fecha,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar GET
                        tratarGetCuotasPagos(response);
                        Resolve.enviarBroadcast(context,false, "Listo");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tratarErrores(error);
                    }
                }, cabeceras);
        //}else{
        // Resolve.enviarBroadcast(getContext(),true, "NO INTERNET");
        // }

        // Petición GET

    }


    private void tratarGetCuotasPagos(JSONObject respuesta) {


        try {
            // Crear referencia de lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            Log.e("response",respuesta.toString());

            // Convertir array JSON de descuentos a modelo
            ProcesadorLocal manejadorCuotasPagas = new ProcesadorLocal();
            manejadorCuotasPagas.procesar(respuesta.getJSONArray("historial"));
            manejadorCuotasPagas.procesarOperaciones_Cuota_Paga(ops,cr);

           // Resolve.enviarBroadcast(context,false, "Buscando...");
            // ¿ Hay operaciones por realizar ?
            if (ops.size() > 0) {

                Log.d(TAG, "# Cambios en \'contacto\': " + ops.size() + " ops.");
                // Aplicar batch de operaciones
                cr.applyBatch(Contract.AUTORIDAD, ops);
                // Notificar cambio al content provider
                cr.notifyChange(Contract.URI_CONTENIDO_BASE, null, false);


            } else {
                Log.d(TAG, "Sin cambios remotos");
                Resolve.enviarBroadcast(context,false, "Listo!!!");
            }

            // Sincronización remota



        } catch (RemoteException | OperationApplicationException | JSONException e) {
            e.printStackTrace();
        }

    }




    private void tratarErrores(VolleyError error) {
        // Crear respuesta de error por defecto
        //logoutUser(getContext());

        RespuestaApi respuesta = new RespuestaApi(ESTADO_PETICION_FALLIDA,
                "Petición incorrecta");
        NetworkResponse response = error.networkResponse;
        String json = null;

        // Verificación: ¿La respuesta tiene contenido interpretable?
        if (error.networkResponse != null) {

            String s = new String(error.networkResponse.data);
            try {
                respuesta = gson.fromJson(s, RespuestaApi.class);
            } catch (JsonSyntaxException e) {
                Log.d(TAG, "Error de parsing: " + s);
            }

        }

        if(response != null && response.data != null){
            switch(response.statusCode){
                case 400:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) //displayMessage(json);
                        respuesta = new RespuestaApi(response.statusCode
                                , json);
                    break;
                case 401:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) //displayMessage(json);
                        respuesta = new RespuestaApi(response.statusCode
                                , "No hay coincidencias del token");
                    logoutUser(context);
                    break;
                case 500:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) displayMessage(json);
                    respuesta = new RespuestaApi(response.statusCode
                            , json);
                    break;
                case 501:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) //displayMessage(json);
                        respuesta = new RespuestaApi(response.statusCode
                                , json);
                    break;
                case 405:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) //displayMessage(json);
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

        // Error conexión servidor
      /*  if (error instanceof ServerError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR, "Error en el servidor");
        }*/

        if (error instanceof NoConnectionError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR
                    , "Servidor no disponible, prueba mas tarde");
        }

        Log.d(TAG, "Error Respuesta:" + (respuesta != null ? respuesta.toString() : "()")
                + "\nDetalles:" + error.getMessage());

        Resolve.enviarBroadcast(context,false, respuesta.getMensaje());

    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
        //enviarBroadcast(false, toastString);

    }


    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }


    private void logoutUser(Context context) {
        db = new DatabaseHandler(context);
        SessionManager session = new SessionManager(context);
        session.setLogin(false);
        db.deleteCobrador();
        // Launching the login activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }










}

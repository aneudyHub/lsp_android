package com.system.lsp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.system.lsp.fragmentos.FragmentListaCoutas;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.DatabaseHandler;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.provider.ProcesadorLocal;
import com.system.lsp.provider.ProcesadorRemoto;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.ui.Login.LoginActivity;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.utilidades.UTiempo;
import com.system.lsp.web.RESTService;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Sincronizador cliente-servidor 
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();
    private Cursor cursor;
    public OperacionesBaseDatos operacionesBaseDatos;
    // Extras para intent local
    public static final String EXTRA_RESULTADO = "extra.resultado";
    private static final String EXTRA_MENSAJE = "extra.mensaje";

    // Recurso sync (10.0.3.2 -> Genymotion; 10.0.2.2 -> AVD)
    //public static final String URL_SYNC_BATCH = "http://10.0.3.2/api.peopleapp.com/v1/sync";
   // public static final String URL_SYNC_BATCH = "http://192.168.10.77/api.saludmock.com/v1/sync";
    // public static final String URL_SYNC_BATCH = "http://192.168.10.77:80/api.saludmock.com/v1/sync"



    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;


    private DatabaseHandler db;
    private ContentResolver cr;
    private Gson gson = new Gson();
    private ProcesadorRemoto procRemoto = new ProcesadorRemoto();



    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        cr = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        cr = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {

        Log.i(TAG, "Comenzando a sincronizar:" + account);

        // Sincronización local

        syncRemota();
        //syncLocal();

    }

    private void syncLocal() {
        // Construcción de cabeceras
        Log.e("sync local","sincronizando--------------");
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());
        HashMap<String, String> cabeceras = new HashMap<>();
        cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(getContext()));
        String fechaSync="";
         cursor = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(getContext()));
        if (cursor.moveToFirst()) {
            fechaSync = cursor.getString(cursor.getColumnIndex(Contract.Cobrador.SYNC_TIME));
        }
        if (fechaSync==null){
            fechaSync ="0";
        }
        cabeceras.put("sync_time",fechaSync);
        Log.e("ESTADO 107","1");

        boolean t = Resolve.isInternetAvailable();
        Log.e("verifica red==>",String.valueOf(t));
        //if(t){
            new RESTService(getContext()).get(URL.SERVER+URL.SYNC,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Procesar GET
                            tratarGet(response);
                            JSONObject jObj =  response;
                            try {
                                String status = jObj.getString("estado");
                                Log.e("STATUS->>LO",String.valueOf(status));
                                if (status.equals("200")){
                                    Log.e("STATUS->>LO",String.valueOf(status));
                                    operacionesBaseDatos.actualizarSyncTime(UPreferencias.obtenerIdUsuario(getContext()),UTiempo.obtenerFechaHora());
                                    Resolve.enviarBroadcast(getContext(),true, "Sicronizacion Completa!!!");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tratarErrores(error);
                        }
                    }, cabeceras);
       // }else{
          //  Resolve.enviarBroadcast(getContext(),true, "NO INTERNET");
       // }

        // Petición GET

    }

    private void tratarGet(JSONObject respuesta) {


        try {
            // Crear referencia de lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            Log.e("response",respuesta.toString());

            // Convertir array JSON de descuentos a modelo
            ProcesadorLocal manejadorContactos = new ProcesadorLocal();
            manejadorContactos.procesar(respuesta.getJSONArray("clientes"),respuesta.getJSONArray("prestamos"),respuesta.getJSONArray("prestamos_detalles"),respuesta.getJSONArray("cuotas_pagas"));
            manejadorContactos.procesarOperaciones_Clientes(ops, cr);
            manejadorContactos.procesarOperaciones_Prestamos(ops, cr);
            manejadorContactos.procesarOperaciones_Prestamos_Detalle(ops,cr);
            //manejadorContactos.procesarOperaciones_Cuota_Paga(ops,cr);


            // ¿ Hay operaciones por realizar ?
            if (ops.size() > 0) {
                Log.d(TAG, "# Cambios en \'contacto\': " + ops.size() + " ops.");
                // Aplicar batch de operaciones
                cr.applyBatch(Contract.AUTORIDAD, ops);
                // Notificar cambio al content provider
                cr.notifyChange(Contract.URI_CONTENIDO_BASE, null, false);

            } else {
                Log.d(TAG, "Sin cambios remotos");
                Resolve.enviarBroadcast(getContext(),true, "Sicronizacion Completa!!!");
            }

            // Sincronización remota



        } catch (RemoteException | OperationApplicationException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void syncRemota() {
        procRemoto = new ProcesadorRemoto();
        Log.e("sync remoto","sincronizando--------------");
        // Construir payload con todas las operaciones remotas pendientes
        //NetworkResponse response = error.networkResponse;
        Log.e("ESTADO 107","2");
        String datos = procRemoto.crearPayload(cr);

        if (datos != null) {
            Log.d(TAG, "Payload de contactos:" + datos);
            operacionesBaseDatos = OperacionesBaseDatos
                    .obtenerInstancia(getContext());
            HashMap<String, String> cabeceras = new HashMap<>();
            cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(getContext()));
            String fechaSync="";
            cursor = operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(getContext()));
            if (cursor.moveToFirst()) {
                fechaSync = cursor.getString(cursor.getColumnIndex(Contract.Cobrador.SYNC_TIME));
            }
            cabeceras.put("sync_time", "0");

            boolean t = Resolve.isInternetAvailable();

            //if(t){
                new RESTService(getContext()).post(URL.SERVER+URL.SYNC, datos,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                    tratarPost();
                                JSONObject jObj =  response;
                                try {
                                    int status = jObj.getInt("status");
                                    Log.e("STATUS->>RE",String.valueOf(status));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tratarErrores(error);
                            }
                        }
                        , cabeceras);
            //}else{
               // Resolve.enviarBroadcast(getContext(),true, "NO INTERNET");
            //}


        } else {
            Log.d(TAG, "Sin cambios locales");
            Resolve.enviarBroadcast(getContext(),true, "Sicronizando espere!!!");
            //syncLocal();

        }



        syncLocal();
    }


    private void tratarPost() {
        // Desmarcar inserciones locales
        procRemoto.desmarcarContactos(cr);
        //Resolve.sincronizarData(getContext());
        syncLocal();
        Resolve.enviarBroadcast(getContext(),true, "Sicronizando espereeeeeeee!!!");
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

                    logoutUser(getContext());
                   // FragmentListaCoutas.stoptimertask();
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
                case 200:
                    syncLocal();
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

        Resolve.enviarBroadcast(getContext(),false, respuesta.getMensaje());

    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(getContext(), toastString, Toast.LENGTH_LONG).show();
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
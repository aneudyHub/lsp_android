package com.system.lsp.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa el acceso al servicio web REST del servidor
 */
public class RESTService {

    private static final String TAG = RESTService.class.getSimpleName();

    private final Context contexto;


    public RESTService(Context contexto) {
        this.contexto = contexto;
    }

    public void get(String uri, Response.Listener<JSONObject> jsonListener,
                    Response.ErrorListener errorListener,
                    final HashMap<String, String> cabeceras) {

        // Crear petición GET
        JsonObjectRequest peticion = new JsonObjectRequest(
                uri,
                null,
                jsonListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.e("ESTA ES LA CABECERA",cabeceras.toString());
                return cabeceras;
            }
        };

        // Añadir petición a la pila
        // Añadir petición a la pila
        peticion.setRetryPolicy(new DefaultRetryPolicy(36000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        peticion.setShouldCache(false);
        VolleySingleton.getInstance(contexto).addToRequestQueue(peticion);
    }

    public void post(String uri, String datos, Response.Listener<JSONObject> jsonListener,
                     Response.ErrorListener errorListener, final HashMap<String, String> cabeceras) {

        // Crear petición POST
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                uri,
                datos,
                jsonListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return cabeceras;
            }
        };

        // Añadir petición a la pila
        peticion.setRetryPolicy(new DefaultRetryPolicy(36000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        peticion.setShouldCache(false);
        VolleySingleton.getInstance(contexto).addToRequestQueue(peticion);
    }

}

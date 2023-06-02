package com.system.lsp.ui.Login;
/**
 * Created by Suarez on 13/06/2017.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.system.lsp.R;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.utilidades.AppController;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.utilidades.UWeb;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONException;
import org.json.JSONObject;

public class Autentication extends Activity {
    private static final String TAG = Autentication.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputCodigo;
    private EditText inputCodigo1;
    private ProgressDialog pDialog;
    private SessionManager session;
    private TextView nombreUsuario;
    TelephonyManager mngr;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;

    private Gson gson = new Gson();

    public void setView() {

        inputCodigo = (EditText) findViewById(R.id.codigo);
        inputCodigo1 = (EditText) findViewById(R.id.codigo1);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (UWeb.hayConexion(Autentication.this)) {
                        String cod1 = inputCodigo1.getText().toString().trim();
                        String cod0 = inputCodigo.getText().toString().trim();

                    // Check for empty data in the form
                    if (!cod1.isEmpty()&&!cod0.isEmpty()) {
                        // login user
                        try {
                            String codigo = cod1+"-"+cod0;
                            Log.e("Valores", "" + codigo);
                            Log.e("VALOR IMEI", getIMEIDeviceId(Autentication.this));//getIMEIDeviceId





                            checkLogin(getIMEIDeviceId(Autentication.this), codigo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Favor digite el codigo Completo!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {

                     /*Snackbar.make(findViewById(R.id.coordinador),
                            "No hay conexion disponible",
                            Snackbar.LENGTH_LONG).show();*/

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Autentication.this);
                    // set title
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>ERROR</font>"));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("NO TIENE INTERNET.<br/><br/>" +
                                    "<font color='#FF0000'> Porfavor apague el MODO AVION o conectese a traves de WIFI o 3G</font>"))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            }

        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antentication);


        // Session manager
        //session = new SessionManager(getApplicationContext());
        Log.e("estoy en este lado1","pendejo1");
        // Check if user is already logged in or not
        if (UPreferencias.obtenerUrlAPP(Autentication.this) != null) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Autentication.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setView();
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String imei, final String codigo) throws JSONException {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        Log.e("PASO 1", "PASO 10");

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("imei", imei);
        jsonBody.put("code", codigo);

        pDialog.setMessage("Verificando ...");
        showDialog();

        Log.e("PASO 2", "PASO 20");
        Log.e("PASO 2", URL.SAUTORISAR + URL.AUTORIZACION);

        StringRequest strReq = new StringRequest(Method.POST,
                URL.SAUTORISAR + URL.AUTORIZACION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                Log.e("PASO 3", "PASO 3");

                try {
                    JSONObject jObj = new JSONObject(response.replaceAll("[^\\x00-\\x7F]", ""));
                   // int status = jObj.getInt("status");
                    Log.e("PASO 4", "PASO 4");
                    // Check for error node in json
                   /* if (status == 200) {*/
                        // user successfully logged in
                        // Create login session
                       // session.setLogin(true);

                        Log.e("PASO 5", "PASO 5");

                        // Now store the user in SQLite
                        //String uid = jObj.getString("id");

                        // JSONObject user = jObj.getJSONObject("user");
                        String urlAPP = jObj.getString("connection");
                        UPreferencias.guardaUrlAPP(Autentication.this,urlAPP);

                        // Launch main activity
                        Intent intent = new Intent(Autentication.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                   /* } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }*/
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                String json = null;
                //tratarErrores(error);

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "mensaje");
                            if (json != null) displayMessage(json);
                            break;
                        case 401:

                            json = new String(response.data);
                            Log.e("Estoy aca",json);
                            json = trimMessage(json, "mensaje");
                            if (json != null) displayMessage(json);
                            break;
                        case 500:
                            json = new String(response.data);
                            json = trimMessage(json, "mensaje");
                            if (json != null) displayMessage(json);
                            break;
                        case 405:
                            json = new String(response.data);
                            json = trimMessage(json, "mensaje");
                            if (json != null) displayMessage(json);
                            break;
                    }
                    //Additional cases
                }

                hideDialog();
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public void tratarErrores(VolleyError error) {
        // Crear respuesta de error por defecto

        RespuestaApi respuesta = new RespuestaApi(ESTADO_PETICION_FALLIDA,
                "Petición incorrecta");


        // Verificación: ¿La respuesta tiene contenido interpretable?
        if (error.networkResponse != null) {

            String s = new String(error.networkResponse.data);
            try {
                respuesta = gson.fromJson(s, RespuestaApi.class);
            } catch (JsonSyntaxException e) {
                Log.d(TAG, "Error de parsing: " + s);
            }

        }

        if (error instanceof NetworkError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA
                    , "Error en la conexión. Intentalo de nuevo");
        }

        if (error instanceof NetworkError) {
            respuesta = new RespuestaApi(202
                    , "usuario o contraseña invalidos");
        }

        // Error de espera al servidor
        if (error instanceof TimeoutError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA, "Fallo al comunicarse con el servidor, compruebe su conexion.");
        }

        // Error de parsing
        if (error instanceof ParseError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_PARSING, "La respuesta no es formato JSON");
        }

        // Error conexión servidor
        if (error instanceof ServerError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR, "Error en el servidor");
        }

        if (error instanceof NoConnectionError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR
                    , "Servidor no disponible, prueba mas tarde");
        }

        Log.d(TAG, "Error Respuesta:" + (respuesta != null ? respuesta.toString() : "()")
                + "\nDetalles:" + error.getMessage());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Autentication.this);
        alertDialogBuilder.setMessage(respuesta.getMensaje());

        alertDialogBuilder.setPositiveButton("salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(ActividadLogin.this,"You clicked yes button",Toast.LENGTH_LONG).show();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


    public String obtenerImei() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //Menores a Android 6.0
            String imei = getIMEI();
            return imei;
        } else {
            // Mayores a Android 6.0
            String imei = "";
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                //imei = getIMEI();
            } else {
                imei = getIMEI();
            }

            return imei;

        }
    }

    @SuppressLint("HardwareIds")
    private String getIMEI() {
        String imei;
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
             imei="";
        }
        assert tm != null;
        imei = tm.getDeviceId(); // Obtiene el imei  or  "352319065579474";
        Log.e("ESTE ES EL IME",imei);
        return imei;

    }

    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }


    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
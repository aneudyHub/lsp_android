package com.system.lsp.ui.Login;

/**
 * Created by Suarez on 13/06/2017.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.ui.Main.MainActivity;
import com.system.lsp.utilidades.AppController;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.utilidades.UWeb;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();


    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    private TextView nombreUsuario;


    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;

    private Gson gson = new Gson();

    public void setView(){

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if(UWeb.hayConexion(LoginActivity.this)) {
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();


                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        // login user
                        try {
                            Log.e("Valores", "" + email + " " + password);
                            checkLogin(email, password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Favor digites sus credenciales!", Toast.LENGTH_LONG)
                                .show();
                    }
                }else {

                     /*Snackbar.make(findViewById(R.id.coordinador),
                            "No hay conexion disponible",
                            Snackbar.LENGTH_LONG).show();*/

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>ERROR</font>"));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("NO TIENE INTERNET.<br/><br/>" +
                                    "<font color='#FF0000'> Porfavor apague el MODO AVION o conectese a traves de WIFI o 3G</font>") )
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
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
        setContentView(R.layout.activity_login);

        setView();
        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) throws JSONException {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        Log.e("PASO 1","PASO 1");

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", email);
        jsonBody.put("password", password);

        pDialog.setMessage("Logging in ...");
        showDialog();

        Log.e("PASO 2","PASO 2");
        Log.e("PASO 2",URL.SERVER + URL.LOGIN);

        StringRequest strReq = new StringRequest(Method.POST,
                URL.SERVER + URL.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                Log.e("PASO 3","PASO 3");

                try {
                    JSONObject jObj = new JSONObject(response.replaceAll("[^\\x00-\\x7F]", ""));
                    int status = jObj.getInt("status");
                    Log.e("PASO 4","PASO 4");
                    // Check for error node in json
                    if (status == 200) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        Log.e("PASO 5","PASO 5");

                        // Now store the user in SQLite
                        //String uid = jObj.getString("id");

                        // JSONObject user = jObj.getJSONObject("user");
                        String id = jObj.getString("id");
                        UPreferencias.guardarIdUsuario(LoginActivity.this,id);
                        String name = jObj.getString("firstname")+ " " + jObj.getString("lastname");
                        UPreferencias.guardarNombreUsuario(LoginActivity.this,name);
                        String username = jObj.getString("username");
                        String token = jObj.getString("token");
                        String email = jObj.getString("email");
                        String telefono = jObj.getString("celular");
                        UPreferencias.guardarTelefonoCobrador(LoginActivity.this,telefono);



                        // Inserting row in users table
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Contract.Cobrador.COBRADOR_ID,id);
                        contentValues.put(Contract.Cobrador.NOMBRE,name);
                        contentValues.put(Contract.Cobrador.USERNAME,username);
                        contentValues.put(Contract.Cobrador.TOKEN,token);
                        contentValues.put(Contract.Cobrador.EMAIL,email);
                        getContentResolver().insert(Contract.Cobrador.URI_CONTENIDO,contentValues);


                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
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
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if(json != null) displayMessage(json);
                            break;
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if(json != null) displayMessage(json);
                            break;
                        case 500:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if(json != null) displayMessage(json);
                            break;
                        case 405:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if(json != null) displayMessage(json);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
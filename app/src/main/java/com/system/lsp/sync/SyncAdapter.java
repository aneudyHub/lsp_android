package com.system.lsp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sincronizador cliente-servidor
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();
    // Extras para intent local
    public static final String EXTRA_RESULTADO = "extra.resultado";
    private static final String EXTRA_MENSAJE = "extra.mensaje";


    private Handler backgroundHandler;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        HandlerThread handlerThread = new HandlerThread("SyncAdapterThread");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
        Log.i(TAG, "Initialized");
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {
        backgroundHandler.post(new SyncTask(getContext(), account, extras, authority, provider, syncResult));
    }

}
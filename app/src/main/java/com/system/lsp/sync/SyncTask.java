package com.system.lsp.sync;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

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
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.provider.ProcesadorRemoto;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.URL;
import com.system.lsp.web.RESTService;
import com.system.lsp.web.RespuestaApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SyncTask implements Runnable {
    private static final String TAG = SyncTask.class.getSimpleName();
    private Context context;

    private RemoteSyncHandler remoteSyncHandler;
    private LocalSyncHandler localSyncHandler;


    public SyncTask(
            Context context,
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            final SyncResult syncResult
    ) {
        this.context = context;

        localSyncHandler = new LocalSyncHandler(context, new SyncHandlerListener() {
            @Override
            public void onSuccess() {
                Resolve.enviarBroadcast(context, true, "SINCRONIZACION COMPLETADA");
            }

            @Override
            public void onFailure(String msg) {
                Resolve.enviarBroadcast(context, false, msg);
            }
        });

        remoteSyncHandler = new RemoteSyncHandler()
    }

    @Override
    public void run() {
        // remote runs first then local applies
        remoteSyncHandler.run();
    }


}

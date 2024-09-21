package com.system.lsp.sync

import android.content.Context
import android.util.Log
import com.android.volley.NetworkError
import com.android.volley.NetworkResponse
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.system.lsp.utilidades.Resolve
import com.system.lsp.web.RespuestaApi
import org.json.JSONObject

abstract class SyncHandler {
    protected abstract suspend fun sendRequest()
    abstract suspend fun run()
}
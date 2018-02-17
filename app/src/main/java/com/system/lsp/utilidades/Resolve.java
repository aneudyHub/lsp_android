package com.system.lsp.utilidades;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.system.lsp.provider.Contract;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.sync.SyncAdapter;
import com.system.lsp.ui.Login.LoginActivity;

import java.net.InetAddress;

/**
 * Created by Suarez on 04/01/2018.
 */

public class Resolve {

    public static final String EXTRA_RESULTADO = "extra.resultado";
    private static final String EXTRA_MENSAJE = "extra.mensaje";


    public static void sincronizarData(Context context){
        // Verificación para evitar iniciar más de una sync a la vez
        Account cuentaActiva = UCuentas.obtenerCuentaActiva(context);
        if (ContentResolver.isSyncActive(cuentaActiva, Contract.AUTORIDAD)) {
            Log.d("SINCRONIZADOR", "Ignorando sincronización ya que existe una en proceso.");
            return;
        }

        Log.d("SINCRONIZADOR", "Solicitando sincronización manual");
        if(UWeb.hayConexion(context)){
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(cuentaActiva, Contract.AUTORIDAD, bundle);
        }else {
            Log.e("No tien internet","Estoy aca");
            enviarBroadcast(context,true, "NO INTERNET");
        }
    }


    public static void enviarBroadcast(Context context,boolean estado, String mensaje) {
        Intent intentLocal = new Intent(Intent.ACTION_SYNC);
        intentLocal.putExtra(EXTRA_RESULTADO, estado);
        intentLocal.putExtra(EXTRA_MENSAJE, mensaje);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentLocal);
    }

    public static String alinea_centro(String Texto, int Maximo){

        StringBuilder SB = new StringBuilder(Texto);
        Maximo = Math.round((Maximo - Texto.length()) / 2);

        for (Integer x = 0; x < Maximo ; x++ ) {
            SB.insert(0, " ");
        }

        return SB.toString();
    }


    public static String dos_columna(String Texto, Integer Maximo, String Texto_dos){

        StringBuilder SB = new StringBuilder(Texto);
        Integer cantidad = Maximo - Texto.length() - Texto_dos.length();

        if (cantidad > 0) for (Integer x = 0; x < cantidad; x++) SB.append(" ");

        SB.append(Texto_dos);

        return SB.toString();

    }

    public static boolean isInternetAvailable() {
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


}

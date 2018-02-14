package com.herprogramacion.peopleapp.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Utilidades sobre estado y propiedades de la red
 */
public class UWeb {
    public static boolean hayConexion(Context contexto) {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

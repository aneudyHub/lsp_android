package com.herprogramacion.peopleapp.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utilidades para obtener y modificar preferencias
 */
public class UPreferencias {
    private static final String PREFERENCIA_CLAVE_API = "preferencia.claveApi";

    private static final String PREFERENCIA_USER = "preferencia.idCobrador";

    private static final String PREFERENCIA_CLAVE_PRESTAMOS = "preferencia.idPrestamos";

    private static final String PREFERENCIA_USER_NAME = "preferencia.nombreUsuario";
    private static final String PREFERENCIA_USER_TELEFONO = "preferencia.telefonoUsuario";


    private static SharedPreferences getDefaultSharedPreferences(Context contexto) {
        return PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public static void guardarClaveApi(Context contexto, String claveApi) {
        SharedPreferences sp = getDefaultSharedPreferences(contexto);
        sp.edit().putString(PREFERENCIA_CLAVE_API, claveApi).apply();
    }

    public static String obtenerClaveApi(Context contexto) {
        return getDefaultSharedPreferences(contexto).getString(PREFERENCIA_CLAVE_API, null);
    }

    public static void guardarIdUsuario(Context contexto, String idUusario) {
        SharedPreferences sp = getDefaultSharedPreferences(contexto);
        sp.edit().putString(PREFERENCIA_USER, idUusario).apply();
    }

    public static String obtenerIdUsuario(Context context){
        return getDefaultSharedPreferences(context).getString(PREFERENCIA_USER,null);
    }


    public static void guardarNombreUsuario(Context contexto, String nombreUsuario) {
        SharedPreferences sp = getDefaultSharedPreferences(contexto);
        sp.edit().putString(PREFERENCIA_USER_NAME, nombreUsuario).apply();
    }



    public static String obtenerNombreUsuario(Context context){
        return getDefaultSharedPreferences(context).getString(PREFERENCIA_USER_NAME,null);
    }


    public static void guardarIdPrestamos(Context contexto, String idPrestamos) {
        SharedPreferences sp = getDefaultSharedPreferences(contexto);
        sp.edit().putString(PREFERENCIA_CLAVE_PRESTAMOS, idPrestamos).apply();
    }

    public static String obtenerIdPrestamos(Context context){
        return getDefaultSharedPreferences(context).getString(PREFERENCIA_CLAVE_PRESTAMOS,null);
    }

    public static void guardarTelefonoCobrador(Context contexto, String telefonoUsuario) {
        SharedPreferences sp = getDefaultSharedPreferences(contexto);
        sp.edit().putString(PREFERENCIA_USER_TELEFONO, telefonoUsuario).apply();
    }
    public static String obtenerTelefonoCobrador(Context context){
        return getDefaultSharedPreferences(context).getString(PREFERENCIA_USER_TELEFONO,null);
    }
}

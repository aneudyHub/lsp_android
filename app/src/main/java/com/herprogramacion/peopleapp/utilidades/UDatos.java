package com.herprogramacion.peopleapp.utilidades;

import android.database.Cursor;

import java.util.Map;

/**
 * Utilidades para gestión de tipos y datos
 */
public class UDatos {

    public static void agregarStringAMapa(Map<String, Object> mapaContactos, String columna, Cursor c) {
        mapaContactos.put(columna, obtenerStringCursor(c, columna));
    }

    private static String obtenerStringCursor(Cursor c, String columna) {
        return c.getString(c.getColumnIndex(columna));
    }
}

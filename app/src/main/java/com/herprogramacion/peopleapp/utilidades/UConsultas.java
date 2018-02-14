package com.herprogramacion.peopleapp.utilidades;

import android.database.Cursor;


/**
 * Utilidades de queries en SQLite
 */
public class UConsultas {

    public static String obtenerString(Cursor cursor, String columna) {
        return cursor.getString(cursor.getColumnIndex(columna));
    }

    public static int obtenerInt(Cursor cursor, String columna) {
        return cursor.getInt(cursor.getColumnIndex(columna));
    }

    public static float obtenerFloat(Cursor cursor, String columna) {
        return cursor.getFloat(cursor.getColumnIndex(columna));
    }
}


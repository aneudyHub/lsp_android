package com.system.lsp.utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilidades sobre datos temporales
 */
public class UTiempo {
    public static long obtenerTiempoEnMS() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String obtenerTiempo() {
        Date date = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String obtenerFecha() {
        Date date = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String obtenerHora() {
        Date date = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String obtenerFechaHora(){
        Date date = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}

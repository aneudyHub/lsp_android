package com.system.lsp.provider;

import android.net.Uri;

import java.util.Random;
import java.util.UUID;

/**
 * Created by aneudy on 7/7/2017.
 */

public class Contract {


    public static final String CLIENTES="clientes";
    public static final String PRESTAMOS="prestamos";
    public static final String PRESTAMOS_DETALLES="prestamos_detalle";
    public static final String CUOTA_PAGADA ="cuota_paga";
    public static final String COBRADOR="cobradores";



    interface ColumnasSincronizacion {
        String MODIFICADO = "modificado";
        String ELIMINADO = "eliminado";
        String INSERTADO = "insertado";
    }

    interface ColumnasClientes{
        String ID = "id";
        String NOMBRE ="nombre";
        String DOCUMENTO ="documento";
        String TELEFONO ="telefono";
        String CELULAR="celular";
        String FOTO_WEB ="foto_web";
        String FOTO_LOCAL ="foto_local";
        String DIRECCION = "direccion";
        String LAT="lat";
        String LNG ="lng";
        String UPDATE_AT="updated_at";
    }

    interface ColumnasPrestamos{
        String ID = "id";
        String CLIENTE_ID ="clientes_id";
        String CAPITAL ="capital_prestamo";
        String INTERES ="interes";
        String PORCIENTO_MORA="porciento_mora";
        String PLAZO ="plazo";
        String CUOTAS="cantidad_cuotas";
        String FECHA_INICIO="fecha_inicio";
        String FECHA_SALDO="fecha_saldo";
        String ACTIVO ="activo";
        String SALDADO="saldado";
        String UPDATED_AT="updated_at";
    }

    interface ColumnasPrestamosDetalles{
        String ID ="id";
        String PRESTAMO="prestamos_id";
        String CUOTA="n_cuota";
        String CAPITAL="capital";
        String INTERES="interes";
        String MORA ="mora";
        String FECHA ="fecha";
        String DIAS_ATRASADOS ="dias_atrasados";
        String PAGADO ="pagado";
        String ACTIVO ="activo";
        String MONTO_PAGADO="monto_pagado";
        String FECHA_PAGADO="fecha_pagado";
        String ABONO_MORA = "abono_mora";
        String MORA_ACUMULADA = "mora_acumulada";
        String UPDATE_AT="updated_at";
    }

    interface ColumnasCuotasPagas {
        String ID_CUOTA ="id_cuota";
        String ID = "id";
        String FECHA="fecha";
        String COBRADOR_ID = "usuarios_id";
        String NOMBRE_COBRADOR = "nombre_cobrador";
        String NOMBRE_CLIENTE = "nombre_cliente";
        String MONTO="monto";
        String TOTALMORA = "total_mora";
        String PRESTAMO="prestamos_id";
        String FECHA_CONSULTA = "fecha_consulta";
        String CADENA_STRING = "cadena_string";
        String UPDATE_AT="updated_at";
    }




    interface ColumnasCobrador{
        String COBRADOR_ID="id";
        String USERNAME="username";
        String NOMBRE="nombre";
        String ZONA="zona";
        String TOKEN="token";
        String EMAIL="email";
        String NOMBRECOMPANIA="compania_nombre";
        String DIRECCIONCOMPANIA="compania_direccion";
        String TELEFONOCOMPANIA="compania_telefono";
        String RNCCOMPANIA="compania_rnc";
        String NOTACOMPANIA="compania_nota";
        String LEMACOMPANIA="compania_lema";
        String SYNC_TIME="sync_time";
    }

    interface ColumnasListaCuotasHoy{
        String CUOTA="cuota";
        String CUOTA_ID="id";
        String CLIENTE ="cliente";
        String CEDULA ="documento";
        String FECHA="fecha";
        String DIRECCION="direccion";
        String TELEFONO="telefono";
        String CELULAR="celular";
        String TOTAL="total";
        String PRESTAMO="prestamo";
    }



    // Autoridad del Content Provider
    public final static String AUTORIDAD = "com.system.lsp";

    // Uri base
    public final static Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);


    // [TIPOS_MIME]
    public static final String BASE_CONTENIDOS = "lsp.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;


    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }


    public static class Cliente implements ColumnasClientes,ColumnasSincronizacion{
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(CLIENTES).build();

        public static Uri crearUriCliente(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdCliente(){return "CI-" + UUID.randomUUID().toString();}

        public static String obtenerIdCliente(Uri uri){return uri.getLastPathSegment();}

    }

    public static class Prestamo implements ColumnasListaCuotasHoy,ColumnasCobrador,ColumnasPrestamos,ColumnasSincronizacion{
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(PRESTAMOS).build();

        public static Uri crearUriPrestamo(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdPrestamo(){return "PRE-" + UUID.randomUUID().toString();}

        public static String obtenerIdPrestamo(Uri uri){return uri.getLastPathSegment();}

    }

    public static class PrestamoDetalle implements ColumnasPrestamosDetalles,ColumnasSincronizacion{
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(PRESTAMOS_DETALLES).build();

        public static Uri crearUriPrestamoDetalle(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdPrestamoDetalle(){return "PREDET-" + UUID.randomUUID().toString();}

        public static String obtenerIdPrestamoDetalle(Uri uri){return uri.getLastPathSegment();}

    }

    public static class CuotaPaga implements ColumnasCuotasPagas,ColumnasSincronizacion{
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(CUOTA_PAGADA).build();

        public static Uri crearUriCuotaPaga(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static int generarIdCuotasPaga(){

            int min = 600000000;
            int max = 2000000000;

            Random r = new Random();
            int i1 = r.nextInt(max - min + 1) + min;
            return i1;}

        public static String obtenerIdCuotasPaga(Uri uri){return uri.getLastPathSegment();}

    }



    public static class Cobrador implements ColumnasCobrador,ColumnasSincronizacion,ColumnasListaCuotasHoy{
        public static final Uri URI_CONTENIDO = URI_CONTENIDO_BASE.buildUpon().appendPath(COBRADOR).build();

        public static Uri crearUriCobrador(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static String generarIdCobrador(){return "CO-" + UUID.randomUUID().toString();}

        public static String obtenerIdCobrador(Uri uri){return uri.getLastPathSegment();}

    }

}

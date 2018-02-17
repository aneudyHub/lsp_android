package com.system.lsp.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.system.lsp.utilidades.UTiempo;

import java.security.acl.LastOwnerException;

/**
 * Created by aneudy on 7/7/2017.
 */

public class Provider extends ContentProvider {

    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";
    private DatabaseHandler helper;
    private ContentResolver resolver;

    public Provider(){

    }

    // [URI_MATCHER]
    public static final UriMatcher uriMatcher;

    //CASE USE
    public static final int CLIENTE=100;
    public static final int CLIENTE_ID=101;
    public static final int PRESTAMO=200;
    public static final int PRESTAMO_ID=201;
    public static final int PRESTAMO_DETALLE=300;
    public static final int PRESTAMO_DETALLE_ID=301;
    public static final int CUOTA_PAGADA =400;
    public static final int CUOTA_PAGADA_ID =401;
    public static final int LISTA_CUOTAS=402;
    public static final int PAGAR_CUOTA =403;
    public static final int LISTA_PRESTAMO=404;
    public static final int COBRADOR=500;
    public static final int COBRADOR_ID=501;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.CLIENTES,CLIENTE);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.CLIENTES+"/*",CLIENTE_ID);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.PRESTAMOS,PRESTAMO);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.PRESTAMOS+"/*",PRESTAMO_ID);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.COBRADOR+"/HOY/*",LISTA_CUOTAS);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.COBRADOR+"/TODO/*",LISTA_PRESTAMO);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.PRESTAMOS_DETALLES,PRESTAMO_DETALLE);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.PRESTAMOS_DETALLES+"/*",PRESTAMO_DETALLE_ID);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.CUOTA_PAGADA, CUOTA_PAGADA);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.CUOTA_PAGADA +"/*", CUOTA_PAGADA_ID);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.CUOTA_PAGADA +"/P/*", PAGAR_CUOTA);

        uriMatcher.addURI(Contract.AUTORIDAD,Contract.COBRADOR,COBRADOR);
        uriMatcher.addURI(Contract.AUTORIDAD,Contract.COBRADOR+"/*",COBRADOR_ID);


    }


    @Override
    public boolean onCreate() {
        helper = new DatabaseHandler(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase bd = helper.getReadableDatabase();

        int match = uriMatcher.match(uri);

        String id;

        Cursor c = null;

        SQLiteQueryBuilder builder= new SQLiteQueryBuilder();

        //Log.e("VALOR-URI",String.valueOf(uri));

        switch (match){
            case CLIENTE:
                c= bd.query(Contract.CLIENTES,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CLIENTE_ID:
                id =Contract.Cliente.obtenerIdCliente(uri);
                c=bd.query(Contract.CLIENTES,projection,Contract.Cliente.ID +"="+ "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case COBRADOR:
                c=bd.query(Contract.COBRADOR,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PRESTAMO:
               // Log.e("VALOR PRESTAMOS",String.valueOf(match));
              //  Log.e("VALOR PRESTAMOS",String.valueOf(PRESTAMO));
                c=bd.query(Contract.PRESTAMOS,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case LISTA_CUOTAS:
               // Log.e("Estoy en Lista","Cuotas");
                c=bd.rawQuery("select " +
                        "p.id as "+Contract.Cobrador.PRESTAMO+"," +
                        "count(pd.n_cuota) as "+Contract.Cobrador.CUOTA+","+
                        "pd.id as "+Contract.Cobrador.CUOTA_ID+","+
                        "cli.nombre as "+Contract.Cobrador.CLIENTE+","+
                        "cli.documento as "+Contract.Cobrador.CEDULA+","+
                        "pd.fecha as "+Contract.Cobrador.FECHA+","+
                        "cli.direccion as "+Contract.Cobrador.DIRECCION+","+
                        "cli.celular as "+Contract.Cobrador.CELULAR+","+
                        "cli.telefono as "+Contract.Cobrador.TELEFONO+","+
                        "((sum(pd.capital)+sum(pd.interes)+sum(pd.mora))- sum(pd.monto_pagado)) as "+Contract.Cobrador.TOTAL+" "+
                        "from prestamos p " +
                        "join prestamos_detalle pd on p.id=pd.prestamos_id " +
                        "join clientes cli on p.clientes_id=cli.id " +
                        "where pd.pagado=0 and date(pd.fecha) <= '"+ UTiempo.obtenerFecha()+"'"+
                        "group by p.id "+
                        "order by pd.fecha DESC",null);
                break;
            case LISTA_PRESTAMO:
               // Log.e("Estoy en Lista","Prestamo");
                c=bd.rawQuery("select " +
                        "p.id as "+Contract.Cobrador.PRESTAMO+"," +
                        "count(pd.n_cuota) as "+Contract.Cobrador.CUOTA+","+
                        "pd.id as "+Contract.Cobrador.CUOTA_ID+","+
                        "cli.nombre as "+Contract.Cobrador.CLIENTE+","+
                        "pd.fecha as "+Contract.Cobrador.FECHA+","+
                        "cli.direccion as "+Contract.Cobrador.DIRECCION+","+
                        "cli.celular as "+Contract.Cobrador.CELULAR+","+
                        "cli.telefono as "+Contract.Cobrador.TELEFONO+","+
                        "p.capital_prestamo as "+Contract.Prestamo.CAPITAL+","+
                        "(sum(pd.capital)+sum(pd.interes)+sum(pd.mora)-sum(pd.monto_pagado)) as "+Contract.Cobrador.TOTAL+" "+
                        "from prestamos p " +
                        "join prestamos_detalle pd on p.id=pd.prestamos_id " +
                        "join clientes cli on p.clientes_id=cli.id " +
                        "group by p.id",null);
                break;
            case PRESTAMO_ID:
                id = Contract.Prestamo.obtenerIdPrestamo(uri);
                c=bd.query(Contract.PRESTAMOS,projection,Contract.Prestamo.ID +"="+ "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case PRESTAMO_DETALLE:
                c=bd.query(Contract.PRESTAMOS_DETALLES,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PRESTAMO_DETALLE_ID:
                id = Contract.PrestamoDetalle.obtenerIdPrestamoDetalle(uri);
                c=bd.query(Contract.PRESTAMOS_DETALLES,projection,Contract.PrestamoDetalle.PRESTAMO +"="+ "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case CUOTA_PAGADA:
                c=bd.query(Contract.CUOTA_PAGADA,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CUOTA_PAGADA_ID:
                id = Contract.CuotaPaga.obtenerIdCuotasPaga(uri);
                c=bd.query(Contract.CUOTA_PAGADA,projection,Contract.CuotaPaga.COBRADOR_ID +"="+ "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }

        c.setNotificationUri(resolver,uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case CLIENTE:
                return Contract.generarMime(Contract.CLIENTES);
            case CLIENTE_ID:
                return Contract.generarMimeItem(Contract.CLIENTES);
            case COBRADOR:
                return Contract.generarMime(Contract.COBRADOR);
            case COBRADOR_ID:
                return Contract.generarMimeItem(Contract.COBRADOR);
            case PRESTAMO:
                return Contract.generarMime(Contract.PRESTAMOS);
            case PRESTAMO_ID:
                return Contract.generarMimeItem(Contract.PRESTAMOS);
            case PRESTAMO_DETALLE:
                return Contract.generarMime(Contract.PRESTAMOS_DETALLES);
            case PRESTAMO_DETALLE_ID:
                return Contract.generarMimeItem(Contract.PRESTAMOS_DETALLES);
            case CUOTA_PAGADA:
                return Contract.generarMime(Contract.CUOTA_PAGADA);
            case CUOTA_PAGADA_ID:
                return Contract.generarMimeItem(Contract.CUOTA_PAGADA);
            case PAGAR_CUOTA:
                return Contract.generarMimeItem(Contract.CUOTA_PAGADA);

            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "Inserción en " + uri + "( " + values.toString() + " )\n");

        SQLiteDatabase bd = helper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case CLIENTE:
                insertOrUpdateById(bd,uri,Contract.CLIENTES,values);
                resolver.notifyChange(uri,null);
                return Contract.Cliente.crearUriCliente(values.getAsString(Contract.Cliente.ID));

            case COBRADOR:
                //bd.insertOrThrow(Contract.COBRADOR_ID,null,values);
                insertOrUpdateById(bd,uri,Contract.COBRADOR,values);
                resolver.notifyChange(uri,null);
                return Contract.Cobrador.crearUriCobrador(values.getAsString(Contract.Cobrador.COBRADOR_ID));

            case PRESTAMO:
               // bd.insertOrThrow(Contract.PRESTAMOS,null,values);
                insertOrUpdateById(bd,uri,Contract.PRESTAMOS,values);
                resolver.notifyChange(uri,null);
                return Contract.Prestamo.crearUriPrestamo(values.getAsString(Contract.Prestamo.ID));

            case PRESTAMO_DETALLE:
                //bd.insertOrThrow(Contract.PRESTAMOS_DETALLES,null,values);
                insertOrUpdateById(bd,uri,Contract.PRESTAMOS_DETALLES,values);
                resolver.notifyChange(uri,null);
                return Contract.PrestamoDetalle.crearUriPrestamoDetalle(values.getAsString(Contract.PrestamoDetalle.ID));

            case CUOTA_PAGADA:
                Log.e("ENTRE AL PROVIDER","HOLA");
                insertOrUpdateById(bd,uri,Contract.CUOTA_PAGADA,values);
                resolver.notifyChange(uri,null);
                return Contract.CuotaPaga.crearUriCuotaPaga(values.getAsString(Contract.CuotaPaga.ID));
             /* long _id =  bd.insertOrThrow(Contract.CUOTA_PAGADA,null,values);
              if (_id > 0) {
                 // Log.e("ESTOY SON LOS VALORES", "" + values.toString());
                  //insertOrUpdateById(bd,uri,Contract.CUOTA_PAGADA,values);
                  resolver.notifyChange(uri, null,false);

                  return Contract.CuotaPaga.crearUriCuotaPaga(values.getAsString(Contract.CuotaPaga.COBRADOR_ID));
              }
                throw new SQLException("Falla al insertar fila en : " + uri);*/

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);

        }
    }

    private void insertOrUpdateById(SQLiteDatabase db, Uri uri, String table,
                                    ContentValues values) throws SQLException {
        Cursor c = query(uri,new String[]{Contract.Cliente.ID},Contract.Cliente.ID+"=?",new String[]{values.get(Contract.Cliente.ID).toString()},null);

        if(c.getCount()>0){
            c.moveToFirst();
            Uri urinew = Uri.withAppendedPath(uri,c.getString(c.getColumnIndex(Contract.Cliente.ID)));
            int r = update(urinew,values,Contract.Cliente.ID+"=?",new String[]{c.getString(c.getColumnIndex(Contract.Cliente.ID))});
            if(r==0){

            }
        }else{
            db.insertOrThrow(table, null, values);
        }

    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete: " + uri);
       // Log.e("valor presmos_DETALLE",String.valueOf(Contract.PRESTAMOS_DETALLES));
       // Log.e("valor uri prestamos",String.valueOf(Contract.PRESTAMOS));
       // Log.e("valor uri prestamos_id",String.valueOf(PRESTAMO_ID));
        int match = uriMatcher.match(uri);
       // Log.e("VALOR MATCH",String.valueOf(match));
       // Log.e("VALOR ID",String.valueOf(PRESTAMO_ID));
        SQLiteDatabase bd = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (match){
            case COBRADOR:
                afectados = bd.delete(Contract.COBRADOR,null, selectionArgs);
                break;
            case CLIENTE_ID:
                //afectados = bd.delete(Contract.CLIENTES,null, selectionArgs);
                String idCliente= Contract.Cliente.obtenerIdCliente(uri);

                afectados = bd.delete(Contract.CLIENTES,
                        Contract.Cliente.ID + "=" + "\'" + idCliente + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;
            case PRESTAMO_ID:
               //afectados = bd.delete(Contract.PRESTAMOS,null, selectionArgs);
                String idPrestamo = Contract.Prestamo.obtenerIdPrestamo(uri);

                afectados = bd.delete(Contract.PRESTAMOS,
                        Contract.Prestamo.ID + "=" + "\'" + idPrestamo + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;
            case PRESTAMO_DETALLE_ID:
                //afectados = bd.delete(Contract.PRESTAMOS_DETALLES,null, selectionArgs);
                String idPrestamoDetalle = Contract.PrestamoDetalle.obtenerIdPrestamoDetalle(uri);

                afectados = bd.delete(Contract.PRESTAMOS_DETALLES,
                        Contract.PrestamoDetalle.ID + "=" + "\'" + idPrestamoDetalle + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;
            case CUOTA_PAGADA_ID:
                afectados = bd.delete(Contract.CUOTA_PAGADA,null, selectionArgs);
                break;


            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        return afectados;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("ESTOY EN EL METODO","UPDATE");
        SQLiteDatabase db = helper.getWritableDatabase();
        int filasAfectadas;
       // Log.e("VALOR  DE LA URI",uri.toString());

        switch (uriMatcher.match(uri)) {
            /*case CUOTA_PAGADA:

                filasAfectadas = db.update(Contract.CUOTA_PAGADA, values,
                        selection, selectionArgs);

                resolver.notifyChange(uri, null, false);

                break;*/

            case CLIENTE_ID:
                String idCliente = Contract.Cliente.obtenerIdCliente(uri);

                filasAfectadas = db.update(Contract.CLIENTES, values,
                        Contract.Cliente.ID + "=" + "\'" + idCliente + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;


            case PRESTAMO_ID:
                String idPrestamo = Contract.Prestamo.obtenerIdPrestamo(uri);
                Log.e("ESTOY ACA","EL PROBLEMA ES AQUI");
                filasAfectadas = db.update(Contract.PRESTAMOS, values,
                        Contract.Prestamo.ID + "=" + "\'" + idPrestamo + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;

            case PRESTAMO_DETALLE_ID:
                String idPrestamoDetalle = Contract.PrestamoDetalle.obtenerIdPrestamoDetalle(uri);

                filasAfectadas = db.update(Contract.PRESTAMOS_DETALLES, values,
                        Contract.PrestamoDetalle.ID + "=" + "\'" + idPrestamoDetalle + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;

            case CUOTA_PAGADA:
                String idCuotaPagada = Contract.CuotaPaga.obtenerIdCuotasPaga(uri);

                filasAfectadas = db.update(Contract.CUOTA_PAGADA, values,
                        selection,
                        selectionArgs);

                resolver.notifyChange(uri, null, false);


               /* filasAfectadas = db.update(Contract.CUOTA_PAGADA, values,
                        Contract.CuotaPaga.ID + "=" + "\'" + idCuotaPagada + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);*/
                break;

            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        return filasAfectadas;
        //return 0;
    }



}

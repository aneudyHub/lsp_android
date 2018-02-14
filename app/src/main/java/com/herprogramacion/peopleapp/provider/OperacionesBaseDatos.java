package com.herprogramacion.peopleapp.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.herprogramacion.peopleapp.modelo.CuotaPaga;
import com.herprogramacion.peopleapp.modelo.CuotaPendiente;
import com.herprogramacion.peopleapp.ui.Pagos.CuotasAdapter;
import com.herprogramacion.peopleapp.utilidades.UPreferencias;
import com.herprogramacion.peopleapp.utilidades.UTiempo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suarez on 20/01/2018.
 */

public class OperacionesBaseDatos {

    private static DatabaseHandler baseDatos;
    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    public OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context context){
        if (baseDatos == null) {
            baseDatos = new DatabaseHandler(context);
        }
        return instancia;
    }

    private static final String CABECERA_PRESTAMO_JOIN_CLIENTE_Y_DATALLEPRESTAMO = "clientes " +
            "INNER JOIN prestamos " +
            "ON clientes.id = prestamos.clientes_id " +
            "INNER JOIN prestamos_detalle " +
            "ON prestamos_detalle.prestamos_id = prestamos.id";


    private static final String CABECERA_CUOTAS_PAGAS = "cuota_paga ";

    public Cursor ObtenerDatosPrestamoPorId(String id){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String selection = String.format("%s=?", Contract.PRESTAMOS + "." +Contract.Prestamo.ID);
        String[] selectionArgs = {id};
        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PRESTAMO_JOIN_CLIENTE_Y_DATALLEPRESTAMO);
        String[] proyeccion = {
                Contract.CLIENTES + "." + Contract.Cliente.NOMBRE,
                Contract.CLIENTES + "." + Contract.Cliente.DOCUMENTO,
                Contract.PRESTAMOS + "." + Contract.Prestamo.CAPITAL,
                Contract.PRESTAMOS + "." + Contract.Prestamo.CUOTAS,
                Contract.PRESTAMOS + "." + Contract.Prestamo.FECHA_INICIO,
               "(SUM( " + Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.CAPITAL + " ) + " +
                       "SUM(" + Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.INTERES + " ) + " +
                       "SUM(" + Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.MORA + " )) AS capital ",
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.INTERES,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.MORA,
                };

       c = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        //Nos movemos al primer registro de la consulta
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }



    public Cursor ObtenerCuotasPagasPorCobrador(String id,String fecha){
        Log.e("valorFecha","entre");
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String selection = String.format("%s=?", Contract.CUOTA_PAGADA + "." +Contract.CuotaPaga.COBRADOR_ID) +
                " AND "+ String.format("%s=?", Contract.CUOTA_PAGADA + "." +Contract.CuotaPaga.FECHA_CONSULTA);;
        String[] selectionArgs = {id,fecha};
        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_CUOTAS_PAGAS);
        String[] proyeccion = {
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.FECHA,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.PRESTAMO,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.NOMBRE_CLIENTE,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.CADENA_STRING,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.MONTO,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.NOMBRE_COBRADOR,
                //"(SUM( " + Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.MONTO + " )) AS monto ",
        };

        c = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        //Nos movemos al primer registro de la consulta
        /*if (c != null) {
            c.moveToFirst();
        }*/
        return c;

    }


    public Cursor ReimprimirFactura(String nombre){
        Log.e("valorFecha","entre");
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String selection = String.format("%s=?", Contract.CUOTA_PAGADA + "." +Contract.CuotaPaga.NOMBRE_CLIENTE);
        String[] selectionArgs = {nombre};
        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_CUOTAS_PAGAS);
        String[] proyeccion = {
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.FECHA,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.PRESTAMO,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.NOMBRE_CLIENTE,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.CADENA_STRING,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.MONTO,
                Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.NOMBRE_COBRADOR,

                //"(SUM( " + Contract.CUOTA_PAGADA + "." + Contract.CuotaPaga.MONTO + " )) AS monto ",
        };

        c = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        //Nos movemos al primer registro de la consulta
        /*if (c != null) {
            c.moveToFirst();
        }*/
        return c;

    }

      /*Log.e("ID-COBRADOR",""+ UPreferencias.obtenerIdUsuario(this));
        Log.e("NOMBRE-COBRADOR",""+ UPreferencias.obtenerNombreUsuario(this));
        Log.e("NOMBRE CLIENTE",nombreCliente);
        Log.e("CADENA STRING", CuotasAdapter.datos);
        Log.e("FECHA",""+  UTiempo.obtenerTiempo());
        Log.e("FFECHA CONSULTA",""+UTiempo.obtenerFecha());
        Log.e("ID-PRESTAMO",""+  idPrestamos);
        Log.e("MONTO",""+  mMonto.getText().toString());*/



    /*int cuota = data.getColumnIndex(Contract.PrestamoDetalle.CUOTA);
    int capital = data.getColumnIndex(Contract.PrestamoDetalle.CAPITAL);
    int interes = data.getColumnIndex(Contract.PrestamoDetalle.INTERES);
    int mora = data.getColumnIndex(Contract.PrestamoDetalle.MORA);
    int fecha = data.getColumnIndex(Contract.PrestamoDetalle.FECHA);
    int pagado = data.getColumnIndex(Contract.PrestamoDetalle.PAGADO);*/

    public Cursor ObtenerCuotasPendientesOPagadas(String id,String pagado){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String selection = String.format("%s=?", Contract.PRESTAMOS_DETALLES + "." +Contract.PrestamoDetalle.PRESTAMO) +
                " AND "+ String.format("%s=?", Contract.PRESTAMOS_DETALLES + "." +Contract.PrestamoDetalle.PAGADO);
        String[] selectionArgs = {id,pagado};
        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Contract.PRESTAMOS_DETALLES);
        String[] proyeccion = {

                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.CUOTA,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.CAPITAL,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.INTERES,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.MORA,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.FECHA,
                Contract.PRESTAMOS_DETALLES + "." + Contract.PrestamoDetalle.PAGADO,

        };

        c = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        //Nos movemos al primer registro de la consulta
       /* if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
                c.moveToFirst();
            }
        }*/
        return c;

    }


    public List<CuotaPendiente> getCuotaPendiete(String ipPrestamos,String pagado){

        List<CuotaPendiente> list = new ArrayList<>();

        Cursor c = ObtenerCuotasPendientesOPagadas(ipPrestamos,pagado);

        while (c.moveToNext()) {
            CuotaPendiente cuotaPendiente = new CuotaPendiente();

            cuotaPendiente.setCuota(c.getInt(0));
            cuotaPendiente.setCapital(c.getDouble(1));
            cuotaPendiente.setInteres(c.getDouble(2));
            cuotaPendiente.setMora(c.getDouble(3));
            cuotaPendiente.setFecha(c.getString(4));
            cuotaPendiente.setPagado(c.getInt(5));

            list.add(cuotaPendiente);

        }
        return  list;

    }



    public List<CuotaPaga> getCutaPagas(String idCobrador,String fecha){

        List<CuotaPaga> list = new ArrayList<>();
        Cursor c = ObtenerCuotasPagasPorCobrador(idCobrador,fecha);
        while (c.moveToNext()) {
            CuotaPaga cuotaPaga = new CuotaPaga();
            cuotaPaga.setFecha(c.getString(0));
            cuotaPaga.setPrestamoId(c.getInt(1));
            cuotaPaga.setNombreCliente(c.getString(2));
            cuotaPaga.setCadenaString(c.getString(3));
            cuotaPaga.setMonto(c.getDouble(4));
            cuotaPaga.setNombreCobrador(c.getString(5));;
            list.add(cuotaPaga);

        }
        return  list;

    }

    public List<CuotaPaga> getReimprimirFactura(String nombre){

        List<CuotaPaga> list = new ArrayList<>();
        Cursor c = ReimprimirFactura(nombre);
        while (c.moveToNext()) {
            CuotaPaga cuotaPaga = new CuotaPaga();
            cuotaPaga.setFecha(c.getString(0));
            cuotaPaga.setPrestamoId(c.getInt(1));
            cuotaPaga.setNombreCliente(c.getString(2));
            cuotaPaga.setCadenaString(c.getString(3));
            cuotaPaga.setMonto(c.getDouble(4));
            cuotaPaga.setNombreCobrador(c.getString(5));

            list.add(cuotaPaga);

        }
        return  list;

    }



}

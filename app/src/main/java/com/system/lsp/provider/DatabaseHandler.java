package com.system.lsp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.system.lsp.provider.Contract.Cliente;
import com.system.lsp.provider.Contract.Prestamo;
import com.system.lsp.provider.Contract.PrestamoDetalle;
import com.system.lsp.provider.Contract.CuotaPaga;
import com.system.lsp.provider.Contract.Cobrador;


/**
 * Created by aneudy on 7/7/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    static final int VERSION = 1;

    static final String NOMBRE_BD = "lsp.db";
    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Clientes = "CREATE TABLE "+Contract.CLIENTES+ " ("+
                Cliente.ID+" INTEGER PRIMARY KEY,"+
                Cliente.NOMBRE+" TEXT,"+
                Cliente.TELEFONO+" TEXT,"+
                Cliente.CELULAR+" TEXT,"+
                Cliente.DOCUMENTO+" TEXT,"+
                Cliente.FOTO_WEB+" TEXT,"+
                Cliente.FOTO_LOCAL+" TEXT,"+
                Cliente.DIRECCION+" TEXT,"+
                Cliente.LAT+" DOUBLE DEFAULT 0,"+
                Cliente.LNG+" DOUBLE DEFAULT 0,"+
                Cliente.UPDATE_AT+" DATE DEFAULT CURRENT_TIMESTAMP,"+
                Cliente.INSERTADO + " INTEGER DEFAULT 1, " +
                Cliente.MODIFICADO + " INTEGER DEFAULT 0,"+
                Cliente.ELIMINADO + " INTEGER DEFAULT 0 );";

                //" FOREIGN KEY ("+Cliente.COBRADOR_ID+") REFERENCES "+Contract.COBRADORES+"("+Contract.Cobrador.ID+"));";
        Log.e("Cliente table",Clientes);
        db.execSQL(Clientes);

        String Prestamos = "CREATE TABLE "+Contract.PRESTAMOS+ " ("+
                Prestamo.ID+" INTEGER PRIMARY KEY,"+
                Prestamo.CLIENTE_ID+ " INTEGER,"+
                Prestamo.CAPITAL+" DOUBLE,"+
                Prestamo.INTERES+" FLOAT,"+
                Prestamo.PORCIENTO_MORA+" INTEGER,"+
                Prestamo.PLAZO+" TEXT,"+
                Prestamo.CUOTAS+" INTEGER,"+
                Prestamo.FECHA_INICIO+" TIMESTAMP,"+
                Prestamo.FECHA_SALDO+" TIMESTAMP,"+
                Prestamo.SALDADO+" INTEGER,"+
                Prestamo.ACTIVO+" INTEGER,"+
                Prestamo.UPDATED_AT+" DATE DEFAULT CURRENT_TIMESTAMP,"+
                Prestamo.INSERTADO+" INTEGER DEFAULT 1,"+
                Prestamo.MODIFICADO+" INTEGER DEFAULT 0,"+
                Prestamo.ELIMINADO+" INTEGER DEFAULT 0 );";

        Log.e("prestamo table",Prestamos);
        db.execSQL(Prestamos);


        String PrestamosDetalles = "CREATE TABLE "+Contract.PRESTAMOS_DETALLES+" ("+
                PrestamoDetalle.ID+" INTEGER PRIMARY KEY,"+
                PrestamoDetalle.PRESTAMO+" INTEGER,"+
                PrestamoDetalle.CUOTA+" INTEGER,"+
                PrestamoDetalle.CAPITAL+" DOUBLE,"+
                PrestamoDetalle.INTERES+" DOUBLE,"+
                PrestamoDetalle.MORA+" DOUBLE,"+
                PrestamoDetalle.FECHA+" DATE,"+
                PrestamoDetalle.DIAS_ATRASADOS+" INTEGER,"+
                PrestamoDetalle.FECHA_PAGADO+" DATE,"+
                PrestamoDetalle.PAGADO+" INTEGER,"+
                PrestamoDetalle.ACTIVO+" INTEGER,"+
                PrestamoDetalle.MONTO_PAGADO+" DOUBLE,"+
                PrestamoDetalle.ABONO_MORA+" DOUBLE,"+
                PrestamoDetalle.MORA_ACUMULADA+" DOUBLE,"+
                PrestamoDetalle.UPDATE_AT + " DATE DEFAULT CURRENT_TIMESTAMP,"+
                Prestamo.INSERTADO+" INTEGER DEFAULT 1,"+
                Prestamo.MODIFICADO+" INTEGER DEFAULT 0,"+
                Prestamo.ELIMINADO+" INTEGER DEFAULT 0 );";
        Log.e("prestamoDetale table",PrestamosDetalles);
        db.execSQL(PrestamosDetalles);


        String PagosT = "CREATE TABLE "+Contract.CUOTA_PAGADA +" ("+
                CuotaPaga.ID_CUOTA + " INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                CuotaPaga.ID + " INTEGER DEFAULT 0,"+
                CuotaPaga.FECHA+" TIMESTAMP,"+
                CuotaPaga.COBRADOR_ID +" INTEGER,"+
                CuotaPaga.NOMBRE_COBRADOR +" TEXT,"+
                CuotaPaga.NOMBRE_CLIENTE +" TEXT,"+
                CuotaPaga.MONTO+" DECIMAL(12,2),"+
                CuotaPaga.TOTALMORA+" DECIMAL(12,2),"+
                CuotaPaga.PRESTAMO+" INTEGER,"+
                CuotaPaga.FECHA_CONSULTA +" TEXT,"+
                CuotaPaga.UPDATE_AT+ " DATE DEFAULT CURRENT_TIMESTAMP,"+
                CuotaPaga.CADENA_STRING +" TEXT,"+
                CuotaPaga.INSERTADO+" INTEGER DEFAULT 1,"+
                CuotaPaga.ELIMINADO+" INTEGER DEFAULT 0,"+
                CuotaPaga.MODIFICADO+" INTEGER DEFAULT 0);";
        Log.e("PagosT table",PagosT);
        db.execSQL(PagosT);

        String CobradorT ="CREATE TABLE "+Contract.COBRADOR+"("+
                Cobrador.COBRADOR_ID+" INTEGER PRIMARY KEY,"+
                Cobrador.USERNAME+" TEXT,"+
                Cobrador.NOMBRE+" TEXT,"+
                Cobrador.ZONA+" TEXT,"+
                Cobrador.TOKEN+" TEXT,"+
                Cobrador.EMAIL+" TEXT,"+
                Cobrador.SYNC_TIME+" TIMESTAMP);";

        db.execSQL(CobradorT);

        Log.e("DB","tables created");





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + Contract.CLIENTES);
            db.execSQL("DROP TABLE IF EXISTS " + Contract.PRESTAMOS);
            db.execSQL("DROP TABLE IF EXISTS " + Contract.PRESTAMOS_DETALLES);
            db.execSQL("DROP TABLE IF EXISTS " + Contract.CUOTA_PAGADA);
            db.execSQL("DROP TABLE IF EXISTS " + Contract.COBRADOR);
        } catch (SQLiteException e) {
            // Manejo de excepciones
            FirebaseCrash.report(e);
        }
        onCreate(db);
    }


    public void deleteCobrador() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Contract.COBRADOR, null, null);
        db.close();

        Log.d("User", "Deleted all user info from sqlite");
    }
}

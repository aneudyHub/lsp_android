package com.system.lsp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.system.lsp.utilidades.UConsultas;
import com.system.lsp.utilidades.UDatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actua como un transformador desde SQLite a JSON para enviar contactos al servidor
 */
public class ProcesadorRemoto {
    private static final String TAG = ProcesadorRemoto.class.getSimpleName();

    // Campos JSON
    private static final String INSERCIONES = "inserciones";
    private static final String MODIFICACIONES = "modificaciones";
    private static final String ELIMINACIONES = "eliminaciones";

    private Gson gson = new Gson();


    public String crearPayload(ContentResolver cr) {
        HashMap<String, Object> payload = new HashMap<>();

        List<Map<String, Object>> inserciones = obtenerInserciones(cr);
        //List<Map<String, Object>> modificaciones = obtenerModificaciones(cr);
        //List<String> eliminaciones = obtenerEliminaciones(cr);

        List<Map<String, Object>> modificaciones = null;
        List<String> eliminaciones = null;

        // Verificación: ¿Hay cambios locales?
        if (inserciones == null && modificaciones == null && eliminaciones == null) {
            return null;
        }

        payload.put(INSERCIONES, inserciones);
        //payload.put(MODIFICACIONES, modificaciones);
        //payload.put(ELIMINACIONES, eliminaciones);

        Log.e("payload--->",payload.toString());
        return gson.toJson(payload);
    }

    public List<Map<String, Object>> obtenerInserciones(ContentResolver cr) {
        List<Map<String, Object>> ops = new ArrayList<>();

        // Obtener contactos donde 'insertado' = 1
        /*Cursor c = cr.query(Contract.CuotaPaga.URI_CONTENIDO,
                null,
                null,
                null, null);*/

        Cursor c = cr.query(Contract.CuotaPaga.URI_CONTENIDO,
                null,
                Contract.CuotaPaga.INSERTADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Inserciones remotas: " + c.getCount());

            // Procesar inserciones
            while (c.moveToNext()) {
                ops.add(mapearInsercion(c));
            }

            return ops;

        } else {
            return null;
        }

    }

    public List<Map<String, Object>> obtenerModificaciones(ContentResolver cr) {

        List<Map<String, Object>> ops = new ArrayList<>();

        // Obtener contactos donde 'modificado' = 1
        Cursor c = cr.query(Contract.Cliente.URI_CONTENIDO,
                null,
                Contract.Cliente.MODIFICADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Existen " + c.getCount() + " modificaciones de contactos");

            // Procesar operaciones
            while (c.moveToNext()) {
                ops.add(mapearActualizacion(c));
            }

            return ops;

        } else {
            return null;
        }

    }

    public List<String> obtenerEliminaciones(ContentResolver cr) {

        List<String> ops = new ArrayList<>();

        // Obtener contactos donde 'eliminado' = 1
        Cursor c = cr.query(Contract.Cliente.URI_CONTENIDO,
                null,
                Contract.Cliente.ELIMINADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Existen " + c.getCount() + " eliminaciones de contactos");

            // Procesar operaciones
            while (c.moveToNext()) {
                ops.add(UConsultas.obtenerString(c, Contract.Cliente.ID));
            }

            return ops;

        } else {
            return null;
        }

    }


    /**
     * Desmarca los contactos locales que ya han sido sincronizados
     *
     * @param cr content resolver
     */
    public void desmarcarContactos(ContentResolver cr) {
        // Establecer valores de la actualización
        ContentValues valores = new ContentValues();
        valores.put(Contract.CuotaPaga.INSERTADO, 0);
        valores.put(Contract.CuotaPaga.MODIFICADO, 0);

        String seleccion = Contract.CuotaPaga.INSERTADO + " = ? OR " +
                Contract.CuotaPaga.MODIFICADO + "= ?";
        String[] argumentos = {"1", "1"};

        // Modificar banderas de insertados y modificados
        cr.update(Contract.CuotaPaga.URI_CONTENIDO, valores, seleccion, argumentos);

        seleccion = Contract.CuotaPaga.ELIMINADO + "=?";
        // Eliminar definitivamente
        //cr.delete(Contract.CuotaPaga.URI_CONTENIDO, seleccion, new String[]{"1"});

    }

    private Map<String, Object> mapearInsercion(Cursor c) {
        // Nuevo mapa para reflejarlo en JSON
        Map<String, Object> mapaContacto = new HashMap<String, Object>();

        // Añadir valores de columnas como atributos
        UDatos.agregarStringAMapa(mapaContacto,Contract.CuotaPaga.COBRADOR_ID, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.CuotaPaga.FECHA, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.CuotaPaga.PRESTAMO, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.CuotaPaga.MONTO, c);
        return mapaContacto;
    }

    private Map<String, Object> mapearActualizacion(Cursor c) {
        // Nuevo mapa para reflejarlo en JSON
        Map<String, Object> mapaContacto = new HashMap<String, Object>();

        // Añadir valores de columnas como atributos
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.ID, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.NOMBRE, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.DOCUMENTO, c);
        UDatos.agregarStringAMapa(mapaContacto,Contract.Cliente.FOTO_LOCAL , c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.FOTO_WEB, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.DIRECCION, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.LAT, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.LNG, c);
        UDatos.agregarStringAMapa(mapaContacto, Contract.Cliente.UPDATE_AT, c);

        return mapaContacto;
    }
}

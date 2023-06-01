package com.system.lsp.provider;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.system.lsp.modelo.Cliente;
import com.system.lsp.modelo.CuotaPaga;
import com.system.lsp.modelo.Prestamo;
import com.system.lsp.modelo.PrestamoDetalle;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Elemento que controla la transformación de JSON a POJO y viceversa
 */
public class ProcesadorLocal {

    private static final String TAG = ProcesadorLocal.class.getSimpleName();



    // Mapa para filtrar solo los elementos a sincronizar
    private HashMap<Integer, Cliente> ClientesRemotos = new HashMap<>();
    private HashMap<Integer,Prestamo> PrestamosRemotos= new HashMap<>();
    private HashMap<Integer,PrestamoDetalle> PrestamosDetalleRemotos= new HashMap<>();
    private HashMap<Integer,CuotaPaga> CuotasPagasRemotos= new HashMap<>();




    // Conversor JSON
    private Gson gson = new Gson();

    public ProcesadorLocal() {
    }


    public void procesar(JSONArray Clientes,JSONArray Prestamos,JSONArray PrestamosDetalles,JSONArray CuotasPagas) throws JSONException {
        // Añadir elementos convertidos a los contactos remotos

        for(int i=0;i<Clientes.length();i++){
            JSONObject row = Clientes.getJSONObject(i);
            Cliente cliente= new Cliente();
            cliente.setId(row.getInt("id"));
            cliente.setNombre(row.getString("nombre_completo"));
            cliente.setDocumento(row.getString("documento"));
            cliente.setTelefono(row.getString("telefono"));
            cliente.setCelular(row.getString("celular"));
            cliente.setFotoLocal("");
            cliente.setFotoWeb(row.getString("foto"));
            cliente.setLat(row.getString("lat")== null ? 0.0:row.getDouble("lat"));
            cliente.setLng(row.getString("lng")== null ? 0.0:row.getDouble("lng"));
            cliente.setDireccion(row.getString("direccion"));
            cliente.setUpdated_at(row.getString("updated_at"));
            cliente.aplicarSanidad();
            ClientesRemotos.put(cliente.getId(), cliente);
            //Log.e("cliente",cliente.toString());
        }

        for (int i=0;i<Prestamos.length();i++){
            JSONObject row = Prestamos.getJSONObject(i);
            Prestamo prestamo= new Prestamo();
            prestamo.setId(row.getInt("id"));
            prestamo.setClientes_Id(row.getInt("clientes_id"));
            prestamo.setCapital(row.getDouble("capital"));
            prestamo.setPorcientoInteres(Float.parseFloat(row.getString("porciento_interes")));
            prestamo.setPorcientoMora(Float.parseFloat(row.getString("porciento_mora")));
            prestamo.setPlazo(row.getString("plazo"));
            prestamo.setCuotas(row.getInt("cuotas"));
            prestamo.setFechaInicio(row.getString("fecha_inicio"));
            prestamo.setFechaSaldo(row.getString("fecha_saldo"));
            prestamo.setActivo(row.getInt("activo"));
            prestamo.setSaldado(row.getInt("saldado"));
            prestamo.setUpdated_at(row.getString("updated_at"));
            prestamo.aplicarSanidad();
            PrestamosRemotos.put(prestamo.getId(),prestamo);
            //Log.e("prestamo",prestamo.toString());
        }



        for (int i=0; i <PrestamosDetalles.length(); i++){
            JSONObject row = PrestamosDetalles.getJSONObject(i);
            PrestamoDetalle prestamosDetalle = new PrestamoDetalle();
            prestamosDetalle.setId(row.getInt("id"));
            prestamosDetalle.setPrestamoId(row.getInt("prestamos_id"));
            prestamosDetalle.setCuota(row.getInt("cuota"));
            prestamosDetalle.setCapital(row.getDouble("capital"));
            prestamosDetalle.setInteres(Float.parseFloat(row.getString("interes")));
            prestamosDetalle.setMora(Float.parseFloat(row.getString("mora")));
            prestamosDetalle.setFecha(row.getString("fecha"));
            prestamosDetalle.setDias_atrasados(row.getString("dias"));
            prestamosDetalle.setFechaPagado(row.getString("fecha_pagado"));
            prestamosDetalle.setPagado(row.getInt("pagado"));
            prestamosDetalle.setActivo(row.getInt("activo"));
            prestamosDetalle.setMontoPagado(row.getDouble("monto_pagado"));
            prestamosDetalle.setAbono_mora(row.getDouble("abono_mora"));
            prestamosDetalle.setMora_acumulada(row.getDouble("mora_acumulada"));
            prestamosDetalle.setUpdated_at(row.getString("updated_at"));
            prestamosDetalle.aplicarSanidad();
            PrestamosDetalleRemotos.put(prestamosDetalle.getId(),prestamosDetalle);
            //Log.e("prestamoDetalle",prestamosDetalle.toString());
        }



        /*for (int i=0; i < CuotasPagas.length(); i++){
            JSONObject row = CuotasPagas.getJSONObject(i);
            CuotaPaga cuotaPaga = new CuotaPaga();
            cuotaPaga.setId(row.getInt("cuota_id"));
            cuotaPaga.setCobradorId(row.getInt("usuarios_id"));
            cuotaPaga.setFecha(row.getString("fecha"));
            cuotaPaga.setPrestamoId(row.getInt("prestamos_id"));
            cuotaPaga.setMonto(row.getDouble("monto"));
            CuotasPagasRemotos.put(cuotaPaga.getId(),cuotaPaga);
            Log.e("CuotasPagas",cuotaPaga.toString());
        }*/



    }


    public void procesar(JSONArray cutasPagas) throws JSONException {
        // Añadir elementos convertidos a los contactos remotos

        for (int i=0; i < cutasPagas.length(); i++){
            JSONObject row = cutasPagas.getJSONObject(i);
            CuotaPaga cuotaPaga = new CuotaPaga();
            cuotaPaga.setId(row.getInt("id"));
            cuotaPaga.setFecha(row.getString("fecha"));
            cuotaPaga.setCobradorId(row.getInt("usuario"));
            cuotaPaga.setNombreCobrador(row.getString("cobrador"));
            cuotaPaga.setNombreCliente(row.getString("cliente"));
            cuotaPaga.setMonto(row.getDouble("monto"));
            cuotaPaga.setTotalMora(row.getDouble("total_mora"));
            cuotaPaga.setPrestamoId(row.getInt("prestamos"));
            cuotaPaga.setFechaConsulta(row.getString("fecha_consulta"));
            cuotaPaga.setUpdated_at(row.getString("updated_at"));
            cuotaPaga.setCadenaString(row.getString("cadena"));
            CuotasPagasRemotos.put(cuotaPaga.getId(),cuotaPaga);
            Log.e("CuotasPagas",cuotaPaga.toString());
        }


    }

    public void procesarOperaciones_Clientes(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        // Consultar clientes locales
        Cursor c_clientes = resolver.query(Contract.Cliente.URI_CONTENIDO,
                null,
                Contract.Cliente.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c_clientes != null) {

            while (c_clientes.moveToNext()) {

                // Convertir fila del cursor en objeto Contacto
                Cliente filaActual = deCursorACliente(c_clientes);

                // Buscar si el contacto actual se encuentra en el mapa de mapacontactos
                Cliente match = ClientesRemotos.get(filaActual.getId());

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    ClientesRemotos.remove(filaActual.getId());

                    // Crear uri de este contacto
                    Uri updateUri = Contract.Cliente.crearUriCliente(String.valueOf(filaActual.getId()));

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                            Log.d(TAG, "Programar actualización  DE CLIENTES " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1) {
                                match.setModificado(0);
                            }
                            ops.add(construirOperacionUpdate_Cliente(match, updateUri));

                        }

                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contract.Cliente.crearUriCliente(String.valueOf(filaActual.getId()));
                    Log.i(TAG, "Programar Eliminación del Clientes " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c_clientes.close();
        }
        // Insertar los items resultantes ya que se asume que no existen en el contacto
        for (Cliente cliente : ClientesRemotos.values()) {
            Log.d(TAG, "Programar Inserción de un nuevo Clientes con ID = " + cliente.getId());
            ops.add(construirOperacionInsert_Cliente(cliente));
        }
    }

    public void procesarOperaciones_Prestamos(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        // Consultar clientes locales
        Cursor c_prestamo = resolver.query(Contract.Prestamo.URI_CONTENIDO,
                null,
                Contract.Prestamo.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c_prestamo != null) {

            while (c_prestamo.moveToNext()) {

                // Convertir fila del cursor en objeto Contacto
                Prestamo filaActual = deCursorAPrestamo(c_prestamo);

                // Buscar si el contacto actual se encuentra en el mapa de mapacontactos
                Prestamo match = PrestamosRemotos.get(filaActual.getId());

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    PrestamosRemotos.remove(filaActual.getId());

                    // Crear uri de este contacto
                    Uri updateUri = Contract.Prestamo.crearUriPrestamo(String.valueOf(filaActual.getId()));

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                            Log.e(TAG, "Programar actualización  del Prestamos " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1) {
                                match.setModificado(0);
                            }
                            ops.add(construirOperacionUpdate_Prestamo(match, updateUri));

                        }

                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contract.Prestamo.crearUriPrestamo(String.valueOf(filaActual.getId()));
                    Log.e(TAG, "Programar Eliminación del Prestamos " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c_prestamo.close();
        }
        // Insertar los items resultantes ya que se asume que no existen en el contacto
        for (Prestamo prestamo : PrestamosRemotos.values()) {
            Log.d(TAG, "Programar Inserción de un nuevo Prestamos con ID = " + prestamo.getId());
            ops.add(construirOperacionInsert_Prestamo(prestamo));
        }
    }

    public void procesarOperaciones_Prestamos_Detalle(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        // Consultar clientes locales
        Cursor c_prestamo_detalle = resolver.query(Contract.PrestamoDetalle.URI_CONTENIDO,
                null,
                Contract.PrestamoDetalle.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c_prestamo_detalle != null) {

            while (c_prestamo_detalle.moveToNext()) {

                // Convertir fila del cursor en objeto Contacto
                PrestamoDetalle filaActual = deCursorAPrestamoDetalle(c_prestamo_detalle);

                // Buscar si el contacto actual se encuentra en el mapa de mapacontactos
                PrestamoDetalle match = PrestamosDetalleRemotos.get(filaActual.getId());

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    PrestamosDetalleRemotos.remove(filaActual.getId());

                    // Crear uri de este contacto
                    Uri updateUri = Contract.PrestamoDetalle.crearUriPrestamoDetalle(String.valueOf(filaActual.getId()));

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                           // Log.d(TAG, "Programar actualización  del Prestamos_Detalle " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1) {
                                match.setModificado(0);
                            }
                            ops.add(construirOperacionUpdate_Prestamo_Detalle(match, updateUri));

                        }
                       // Log.d(TAG, "Programar actualización  del Prestamos_Detalle " + updateUri);
                        ops.add(construirOperacionUpdate_Prestamo_Detalle(match, updateUri));
                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contract.PrestamoDetalle.crearUriPrestamoDetalle(String.valueOf(filaActual.getId()));
                    Log.i(TAG, "Programar Eliminación del Prestamos_Detalle " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c_prestamo_detalle.close();
        }
        // Insertar los items resultantes ya que se asume que no existen en el contacto
        for (PrestamoDetalle prestamoDetalle : PrestamosDetalleRemotos.values()) {
           // Log.d(TAG, "Programar Inserción de un nuevo Prestamos_Detalle con ID = " + prestamoDetalle.getId());
            ops.add(construirOperacionInsert_Prestamo_Detalle(prestamoDetalle));
        }
    }



    public void procesarOperaciones_Cuota_Paga(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        Log.e("DONDE ESTOY","1");
        // Consultar clientes locales
        Cursor c_cuotaPagas = resolver.query(Contract.CuotaPaga.URI_CONTENIDO,
                null,
                Contract.CuotaPaga.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c_cuotaPagas != null) {

            while (c_cuotaPagas.moveToNext()) {

                // Convertir fila del cursor en objeto Contacto
                CuotaPaga filaActual = deCursorACuotasPagas(c_cuotaPagas);

                // Buscar si el contacto actual se encuentra en el mapa de mapacontactos
                CuotaPaga match = CuotasPagasRemotos.get(filaActual.getId());

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    CuotasPagasRemotos.remove(filaActual.getId());

                    // Crear uri de este contacto
                    Uri updateUri = Contract.CuotaPaga.crearUriCuotaPaga(String.valueOf(filaActual.getId()));

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                            Log.e(TAG, "Programar actualización  del CuotasPagas " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1) {
                                match.setModificado(0);
                            }
                            ops.add(construirOperacionUpdate_Cuota_Pagada(match, updateUri));

                        }

                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contract.CuotaPaga.crearUriCuotaPaga(String.valueOf(filaActual.getId()));
                    Log.e(TAG, "Programar Eliminación del CuotasPagas " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c_cuotaPagas.close();
        }
        // Insertar los items resultantes ya que se asume que no existen en el contacto
        for (CuotaPaga cuotaPaga : CuotasPagasRemotos.values()) {
            Log.d(TAG, "Programar Inserción Cuotas-Pagas con ID = " + cuotaPaga.getId());
            ops.add(construirOperacionInsert_Cuota_Pagada(cuotaPaga));
        }
    }


    public void procesarOperaciones_HistorialPagos(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        // Consultar clientes locales
        Cursor c_clientes = resolver.query(Contract.Cliente.URI_CONTENIDO,
                null,
                Contract.Cliente.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c_clientes != null) {

            while (c_clientes.moveToNext()) {

                // Convertir fila del cursor en objeto Contacto
                Cliente filaActual = deCursorACliente(c_clientes);

                // Buscar si el contacto actual se encuentra en el mapa de mapacontactos
                Cliente match = ClientesRemotos.get(filaActual.getId());

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    ClientesRemotos.remove(filaActual.getId());

                    // Crear uri de este contacto
                    Uri updateUri = Contract.Cliente.crearUriCliente(String.valueOf(filaActual.getId()));

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                            Log.d(TAG, "Programar actualización  DE CLIENTES " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.getModificado() == 1) {
                                match.setModificado(0);
                            }
                            ops.add(construirOperacionUpdate_Cliente(match, updateUri));

                        }

                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contract.Cliente.crearUriCliente(String.valueOf(filaActual.getId()));
                    Log.i(TAG, "Programar Eliminación del Clientes " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c_clientes.close();
        }
        // Insertar los items resultantes ya que se asume que no existen en el contacto
        for (Cliente cliente : ClientesRemotos.values()) {
            Log.d(TAG, "Programar Inserción de un nuevo Clientes con ID = " + cliente.getId());
            ops.add(construirOperacionInsert_Cliente(cliente));
        }
    }




    private ContentProviderOperation construirOperacionInsert_Cliente(Cliente cliente) {
        return ContentProviderOperation.newInsert(Contract.Cliente.URI_CONTENIDO)
                .withValue(Contract.Cliente.ID,cliente.getId())
                .withValue(Contract.Cliente.NOMBRE,cliente.getNombre())
                .withValue(Contract.Cliente.DOCUMENTO,cliente.getDocumento())
                .withValue(Contract.Cliente.TELEFONO,cliente.getTelefono())
                .withValue(Contract.Cliente.CELULAR,cliente.getCelular())
                .withValue(Contract.Cliente.FOTO_LOCAL,cliente.getFotoLocal())
                .withValue(Contract.Cliente.FOTO_WEB,cliente.getFotoWeb())
                .withValue(Contract.Cliente.DIRECCION,cliente.getDireccion())
                .withValue(Contract.Cliente.LAT,cliente.getLat())
                .withValue(Contract.Cliente.LNG,cliente.getLng())
                .withValue(Contract.Cliente.UPDATE_AT,cliente.getUpdated_at())
                .withValue(Contract.Cliente.INSERTADO,0)
                .build();
    }

    private ContentProviderOperation construirOperacionUpdate_Cliente(Cliente cliente, Uri updateUri) {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(Contract.Cliente.ID,cliente.getId())
                .withValue(Contract.Cliente.NOMBRE,cliente.getNombre())
                .withValue(Contract.Cliente.DOCUMENTO,cliente.getDocumento())
                .withValue(Contract.Cliente.TELEFONO,cliente.getTelefono())
                .withValue(Contract.Cliente.CELULAR,cliente.getCelular())
                .withValue(Contract.Cliente.FOTO_LOCAL,cliente.getFotoLocal())
                .withValue(Contract.Cliente.FOTO_WEB,cliente.getFotoWeb())
                .withValue(Contract.Cliente.DIRECCION,cliente.getDireccion())
                .withValue(Contract.Cliente.LAT,cliente.getLat())
                .withValue(Contract.Cliente.LNG,cliente.getLng())
                .withValue(Contract.Cliente.UPDATE_AT,cliente.getUpdated_at())
                .withValue(Contract.Cliente.MODIFICADO,cliente.getModificado())
                .build();
    }





    private ContentProviderOperation construirOperacionInsert_Prestamo(Prestamo prestamo) {
        return ContentProviderOperation.newInsert(Contract.Prestamo.URI_CONTENIDO)
                .withValue(Contract.Prestamo.ID,prestamo.getId())
                .withValue(Contract.Prestamo.CLIENTE_ID,prestamo.getClientes_Id())
                .withValue(Contract.Prestamo.CAPITAL,prestamo.getCapital())
                .withValue(Contract.Prestamo.INTERES,prestamo.getPorcientoInteres())
                .withValue(Contract.Prestamo.PORCIENTO_MORA,prestamo.getPorcientoMora())
                .withValue(Contract.Prestamo.PLAZO,prestamo.getPlazo())
                .withValue(Contract.Prestamo.CUOTAS,prestamo.getCuotas())
                .withValue(Contract.Prestamo.FECHA_INICIO,prestamo.getFechaInicio())
                .withValue(Contract.Prestamo.FECHA_SALDO,prestamo.getFechaSaldo())
                .withValue(Contract.Prestamo.SALDADO,prestamo.getSaldado())
                .withValue(Contract.Prestamo.ACTIVO,prestamo.getActivo())
                .withValue(Contract.Prestamo.UPDATED_AT,prestamo.getUpdated_at())
                .withValue(Contract.Prestamo.INSERTADO,0)
                .build();
    }

    private ContentProviderOperation construirOperacionUpdate_Prestamo(Prestamo prestamo, Uri updateUri) {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(Contract.Prestamo.CLIENTE_ID,prestamo.getClientes_Id())
                .withValue(Contract.Prestamo.CAPITAL,prestamo.getCapital())
                .withValue(Contract.Prestamo.INTERES,prestamo.getPorcientoInteres())
                .withValue(Contract.Prestamo.PORCIENTO_MORA,prestamo.getPorcientoMora())
                .withValue(Contract.Prestamo.PLAZO,prestamo.getPlazo())
                .withValue(Contract.Prestamo.CUOTAS,prestamo.getCuotas())
                .withValue(Contract.Prestamo.FECHA_INICIO,prestamo.getFechaInicio())
                .withValue(Contract.Prestamo.FECHA_SALDO,prestamo.getFechaSaldo())
                .withValue(Contract.Prestamo.SALDADO,prestamo.getSaldado())
                .withValue(Contract.Prestamo.ACTIVO,prestamo.getActivo())
                .withValue(Contract.Prestamo.UPDATED_AT,prestamo.getUpdated_at())
                .withValue(Contract.Prestamo.MODIFICADO,prestamo.getModificado())
                .build();
    }

     private ContentProviderOperation construirOperacionInsert_Prestamo_Detalle(PrestamoDetalle prestamoDetalle) {
        return ContentProviderOperation.newInsert(Contract.PrestamoDetalle.URI_CONTENIDO)
                .withValue(Contract.PrestamoDetalle.ID,prestamoDetalle.getId())
                .withValue(Contract.PrestamoDetalle.PRESTAMO,prestamoDetalle.getPrestamoId())
                .withValue(Contract.PrestamoDetalle.CUOTA,prestamoDetalle.getCuota())
                .withValue(Contract.PrestamoDetalle.CAPITAL,prestamoDetalle.getCapital())
                .withValue(Contract.PrestamoDetalle.INTERES,prestamoDetalle.getInteres())
                .withValue(Contract.PrestamoDetalle.MORA,prestamoDetalle.getMora())
                .withValue(Contract.PrestamoDetalle.FECHA,prestamoDetalle.getFecha())
                .withValue(Contract.PrestamoDetalle.DIAS_ATRASADOS,prestamoDetalle.getDias_atrasados())
                .withValue(Contract.PrestamoDetalle.PAGADO,prestamoDetalle.getPagado())
                .withValue(Contract.PrestamoDetalle.ACTIVO,prestamoDetalle.getActivo())
                .withValue(Contract.PrestamoDetalle.MONTO_PAGADO,prestamoDetalle.getMontoPagado())
                .withValue(Contract.PrestamoDetalle.ABONO_MORA,prestamoDetalle.getAbono_mora())
                .withValue(Contract.PrestamoDetalle.MORA_ACUMULADA,prestamoDetalle.getMora_acumulada())
                .withValue(Contract.PrestamoDetalle.UPDATE_AT,prestamoDetalle.getUpdated_at())
                .withValue(Contract.PrestamoDetalle.INSERTADO,0)
                .build();


    }


    private ContentProviderOperation construirOperacionUpdate_Prestamo_Detalle(PrestamoDetalle prestamoDetalle, Uri updateUri) {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(Contract.PrestamoDetalle.ID,prestamoDetalle.getId())
                .withValue(Contract.PrestamoDetalle.PRESTAMO,prestamoDetalle.getPrestamoId())
                .withValue(Contract.PrestamoDetalle.CUOTA,prestamoDetalle.getCuota())
                .withValue(Contract.PrestamoDetalle.CAPITAL,prestamoDetalle.getCapital())
                .withValue(Contract.PrestamoDetalle.INTERES,prestamoDetalle.getInteres())
                .withValue(Contract.PrestamoDetalle.MORA,prestamoDetalle.getMora())
                .withValue(Contract.PrestamoDetalle.FECHA,prestamoDetalle.getFecha())
                .withValue(Contract.PrestamoDetalle.DIAS_ATRASADOS,prestamoDetalle.getDias_atrasados())
                .withValue(Contract.PrestamoDetalle.PAGADO,prestamoDetalle.getPagado())
                .withValue(Contract.PrestamoDetalle.ACTIVO,prestamoDetalle.getActivo())
                .withValue(Contract.PrestamoDetalle.MONTO_PAGADO,prestamoDetalle.getMontoPagado())
                .withValue(Contract.PrestamoDetalle.ABONO_MORA,prestamoDetalle.getAbono_mora())
                .withValue(Contract.PrestamoDetalle.MORA_ACUMULADA,prestamoDetalle.getMora_acumulada())
                .withValue(Contract.PrestamoDetalle.UPDATE_AT,prestamoDetalle.getUpdated_at())
                .withValue(Contract.PrestamoDetalle.MODIFICADO,prestamoDetalle.getModificado())
                .build();


    }



    private ContentProviderOperation construirOperacionInsert_Cuota_Pagada(CuotaPaga cuotaPaga) {
        return ContentProviderOperation.newInsert(Contract.CuotaPaga.URI_CONTENIDO)
                .withValue(Contract.CuotaPaga.ID,cuotaPaga.getId())
                .withValue(Contract.CuotaPaga.FECHA,cuotaPaga.getFecha())
                .withValue(Contract.CuotaPaga.COBRADOR_ID,cuotaPaga.getCobradorId())
                .withValue(Contract.CuotaPaga.NOMBRE_COBRADOR,cuotaPaga.getNombreCobrador())
                .withValue(Contract.CuotaPaga.NOMBRE_CLIENTE,cuotaPaga.getNombreCliente())
                .withValue(Contract.CuotaPaga.MONTO,cuotaPaga.getMonto())
                .withValue(Contract.CuotaPaga.TOTALMORA,cuotaPaga.getTotalMora())
                .withValue(Contract.CuotaPaga.PRESTAMO,cuotaPaga.getPrestamoId())
                .withValue(Contract.CuotaPaga.FECHA_CONSULTA,cuotaPaga.getFechaConsulta())
                .withValue(Contract.CuotaPaga.UPDATE_AT,cuotaPaga.getUpdated_at())
                .withValue(Contract.CuotaPaga.CADENA_STRING,cuotaPaga.getCadenaString())
                .withValue(Contract.CuotaPaga.INSERTADO,0)
                .build();


    }

    private ContentProviderOperation construirOperacionUpdate_Cuota_Pagada(CuotaPaga cuotaPaga, Uri updateUri) {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(Contract.CuotaPaga.ID,cuotaPaga.getId())
                .withValue(Contract.CuotaPaga.FECHA,cuotaPaga.getFecha())
                .withValue(Contract.CuotaPaga.COBRADOR_ID,cuotaPaga.getCobradorId())
                .withValue(Contract.CuotaPaga.NOMBRE_COBRADOR,cuotaPaga.getNombreCobrador())
                .withValue(Contract.CuotaPaga.NOMBRE_CLIENTE,cuotaPaga.getNombreCliente())
                .withValue(Contract.CuotaPaga.MONTO,cuotaPaga.getMonto())
                .withValue(Contract.CuotaPaga.PRESTAMO,cuotaPaga.getPrestamoId())
                .withValue(Contract.CuotaPaga.FECHA_CONSULTA,cuotaPaga.getFechaConsulta())
                .withValue(Contract.CuotaPaga.UPDATE_AT,cuotaPaga.getUpdated_at())
                .withValue(Contract.CuotaPaga.CADENA_STRING,cuotaPaga.getCadenaString())
                .withValue(Contract.PrestamoDetalle.MODIFICADO,cuotaPaga.getModificado())
                .build();


    }








    /**
     * Convierte una fila de un Cursor en un nuevo Contacto
     *
     * @param c cursor
     * @return objeto contacto
     */
    private Cliente deCursorACliente(Cursor c) {
        return new Cliente(
                c.getInt(c.getColumnIndex(Contract.Cliente.ID)),
                c.getString(c.getColumnIndex(Contract.Cliente.NOMBRE)),
                c.getString(c.getColumnIndex(Contract.Cliente.TELEFONO)),
                c.getString(c.getColumnIndex(Contract.Cliente.CELULAR)),
                c.getString(c.getColumnIndex(Contract.Cliente.DOCUMENTO)),
                c.getString(c.getColumnIndex(Contract.Cliente.FOTO_LOCAL)),
                c.getString(c.getColumnIndex(Contract.Cliente.FOTO_WEB)),
                c.getString(c.getColumnIndex(Contract.Cliente.DIRECCION)),
                c.getDouble(c.getColumnIndex(Contract.Cliente.LAT)),
                c.getDouble(c.getColumnIndex(Contract.Cliente.LNG)),
                c.getString(c.getColumnIndex(Contract.Cliente.UPDATE_AT)),
                c.getInt(c.getColumnIndex(Contract.Cliente.MODIFICADO))
        );
    }


    /**
     * Convierte una fila de un Cursor en un nuevo Contacto
     *
     * @param c cursor
     * @return objeto contacto
     */
    private Prestamo deCursorAPrestamo(Cursor c) {
        return new Prestamo(
                c.getInt(c.getColumnIndex(Contract.Prestamo.ID)),
                c.getInt(c.getColumnIndex(Contract.Prestamo.CLIENTE_ID)),
                c.getDouble(c.getColumnIndex(Contract.Prestamo.CAPITAL)),
                Float.parseFloat(c.getString(c.getColumnIndex(Contract.Prestamo.INTERES))),
                Float.parseFloat(c.getString(c.getColumnIndex(Contract.Prestamo.PORCIENTO_MORA))),
                c.getString(c.getColumnIndex(Contract.Prestamo.PLAZO)),
                c.getInt(c.getColumnIndex(Contract.Prestamo.CUOTAS)),
                c.getString(c.getColumnIndex(Contract.Prestamo.FECHA_INICIO)),
                c.getString(c.getColumnIndex(Contract.Prestamo.FECHA_SALDO)),
                c.getInt(c.getColumnIndex(Contract.Prestamo.ACTIVO)),
                c.getInt(c.getColumnIndex(Contract.Prestamo.SALDADO)),
                c.getString(c.getColumnIndex(Contract.Prestamo.UPDATED_AT)),
                c.getInt(c.getColumnIndex(Contract.Prestamo.MODIFICADO))

        );
    }


    private PrestamoDetalle deCursorAPrestamoDetalle(Cursor c){
        return new PrestamoDetalle(

                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.ID)),
                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.PRESTAMO)),
                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.CUOTA)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.CAPITAL)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.INTERES)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.MORA)),
                c.getString(c.getColumnIndex(Contract.PrestamoDetalle.FECHA)),
                c.getString(c.getColumnIndex(Contract.PrestamoDetalle.DIAS_ATRASADOS)),
                c.getString(c.getColumnIndex(Contract.PrestamoDetalle.FECHA_PAGADO)),
                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.PAGADO)),
                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.ACTIVO)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.MONTO_PAGADO)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.ABONO_MORA)),
                c.getDouble(c.getColumnIndex(Contract.PrestamoDetalle.MORA_ACUMULADA)),
                c.getString(c.getColumnIndex(Contract.PrestamoDetalle.UPDATE_AT)),
                c.getInt(c.getColumnIndex(Contract.PrestamoDetalle.MODIFICADO))


        );

    }

    private CuotaPaga deCursorACuotasPagas(Cursor c){


        return new CuotaPaga(
                c.getInt(c.getColumnIndex(Contract.CuotaPaga.ID)),
                c.getInt(c.getColumnIndex(Contract.CuotaPaga.COBRADOR_ID)),
                c.getString(c.getColumnIndex(Contract.CuotaPaga.FECHA)),
                c.getInt(c.getColumnIndex(Contract.CuotaPaga.PRESTAMO)),
                c.getDouble(c.getColumnIndex(Contract.CuotaPaga.MONTO)),
                c.getInt(c.getColumnIndex(Contract.CuotaPaga.MODIFICADO))

        );

    }
}

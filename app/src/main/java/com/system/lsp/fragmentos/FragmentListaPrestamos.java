package com.system.lsp.fragmentos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.system.lsp.R;
import com.system.lsp.modelo.DatosCliente;
import com.system.lsp.provider.Contract;
import com.system.lsp.ui.AdaptadorPrestamos;
import com.system.lsp.ui.Prestamo.DetallePrestamo;
import com.system.lsp.utilidades.Resolve;

import java.util.ArrayList;

/**
 * Created by Suarez on 13/01/2018.
 */

public class FragmentListaPrestamos extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdaptadorPrestamos.OnItemClickListener,SearchView.OnQueryTextListener{

    public static final String TAG = "Prestamos";
    // Referencias UI
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPrestamos adaptador;
    private int REQ_DET=100;
    private DatosCliente cli;

    public static String valor;

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filtroSync = new IntentFilter(Intent.ACTION_SYNC);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receptorSync, filtroSync);
    }

    private BroadcastReceiver receptorSync;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_prestamo, container, false);
        setHasOptionsMenu(true);
        prepararLista(view);
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);
        Log.e("broadcast","llego");
        // Crear receptor de mensajes de sincronización
        receptorSync = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //mostrarProgreso(false);
                Log.e("broadcast","llego");
                if(swipeRefreshLayout!=null){
                    swipeRefreshLayout.setRefreshing(false);
                }
                String mensaje = intent.getStringExtra("extra.mensaje");
                Snackbar.make(view.findViewById(R.id.coordinador),
                        mensaje, Snackbar.LENGTH_LONG).show();
            }
        };

        /*swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               Log.e("ESTOY EN onRefresh","");
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

            }
        });*/

        return  view;
    }

    private void sincronizar(View view) {
        swipeRefreshLayout.setRefreshing(true);
        // Verificación para evitar iniciar más de una sync a la vez
        Resolve.sincronizarData(getContext());
    }

    private void mostrarProgreso(boolean mostrar) {
        //findViewById(R.id.barra).setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void prepararLista(View view) {
        Log.d(TAG, "Solicitando sincronización manual");
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getContext());
        reciclador.setLayoutManager(layoutManager);
        //reciclador.setAdapter(adaptador);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(Contract.Cobrador.URI_CONTENIDO,"TODO/1");
        return new CursorLoader(
                getContext(),
                uri,null,null,null,null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.e("cursor listaaaaa",data.toString());
        ArrayList<DatosCliente> mArrayList = new ArrayList<DatosCliente>();


        int cuota = data.getColumnIndex(Contract.Prestamo.CUOTA);
        int cuota_id = data.getColumnIndex(Contract.Prestamo.CUOTA_ID);
        int cliente = data.getColumnIndex(Contract.Prestamo.CLIENTE);
        int cedula = data.getColumnIndex(Contract.Prestamo.CLIENTE);
        int fecha = data.getColumnIndex(Contract.Prestamo.FECHA);
        int direccion = data.getColumnIndex(Contract.Prestamo.DIRECCION);
        int telefono = data.getColumnIndex(Contract.Prestamo.TELEFONO);
        int celular = data.getColumnIndex(Contract.Prestamo.CELULAR);
        int capital = data.getColumnIndex(Contract.Prestamo.CAPITAL);
        int total = data.getColumnIndex(Contract.Prestamo.TOTAL);
        int prestamo = data.getColumnIndex(Contract.Prestamo.PRESTAMO);



        Log.e("El indice del capital",""+capital);

        if (data != null) {
            for (data.moveToFirst(); !data.isAfterLast();data.moveToNext()){

                cli = new DatosCliente(data.getString(cuota),data.getString(cuota_id),
                        data.getString(cliente),data.getString(cedula),data.getString(fecha),data.getString(direccion),
                        data.getString(telefono),data.getString(celular),data.getString(capital),
                        data.getString(total), data.getString(prestamo)
                );
                mArrayList.add(cli);
               // Log.e("Este es el ARRAY",cli.getDIRECCION());
            }



            //items = nuevoCursor;

        }
        adaptador = new AdaptadorPrestamos(this,mArrayList);
        reciclador.setAdapter(adaptador);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void mostrarDetalles(Uri uri,String montoPendiente) {
        Intent intent = new Intent(getActivity(), DetallePrestamo.class);
        if (null != uri) {
            intent.putExtra(Contract.PRESTAMOS, uri.toString());
            intent.putExtra(Contract.Cobrador.TOTAL,montoPendiente);
            intent.putExtra(Contract.Prestamo.ID, Contract.Prestamo.obtenerIdPrestamo(uri));
            intent.putExtra(Contract.Cobrador.CLIENTE,Contract.Prestamo.CLIENTE);

        }

        startActivityForResult(intent,REQ_DET);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

    }




    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adaptador.getFilter().filter(newText);
                return true;
            }
        });
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(String idContacto,String montoPendiente) {
        Log.e("idcontacto",idContacto);
        mostrarDetalles(Contract.Prestamo.crearUriPrestamo(idContacto),montoPendiente);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receptorSync);
    }
}
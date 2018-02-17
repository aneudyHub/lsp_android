package com.system.lsp.fragmentos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.system.lsp.R;
import com.system.lsp.modelo.DatosCliente;
import com.system.lsp.provider.Contract;
import com.system.lsp.ui.AdaptadorCuotas;
import com.system.lsp.ui.Main.MainActivity;
import com.system.lsp.ui.Pagos.Pagos;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.URL;

import java.util.ArrayList;

/**
 * Created by aneudy on 22/6/2017.
 */

public class FragmentListaCoutas extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdaptadorCuotas.OnItemClickListener,SearchView.OnQueryTextListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    private int REQ_DET=100;
    // Referencias UI
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorCuotas adaptador;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout mInfoNoData;
    private BroadcastReceiver receptorSync;


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filtroSync = new IntentFilter(Intent.ACTION_SYNC);
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receptorSync, filtroSync);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_couta, container, false);
        setHasOptionsMenu(true);
        prepararLista(view);
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);
        Log.e("broadcast","llego");
        // Crear receptor de mensajes de sincronización
        receptorSync = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //mostrarProgreso(false);
                Log.e("broadcast","llego0000000000");
                swipeRefreshLayout.setRefreshing(false);
                String mensaje = intent.getStringExtra("extra.mensaje");
                Snackbar.make(view.findViewById(R.id.coordinador),
                        mensaje, Snackbar.LENGTH_LONG).show();

            }
        };


        mInfoNoData =(ConstraintLayout)view.findViewById(R.id.info_data);
        mInfoNoData.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sincronizar(view);
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //sincronizar(view);

            }
        });

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
        Uri uri = Uri.withAppendedPath(Contract.Cobrador.URI_CONTENIDO,"HOY/1");
        return new CursorLoader(
                getContext(),
                uri,null,null,null,null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // Log.e("cursor listaaaaa",data.toString());
        ArrayList<DatosCliente> mArrayList = new ArrayList<DatosCliente>();


        int cuota = data.getColumnIndex(Contract.Cobrador.CUOTA);
        int cuota_id = data.getColumnIndex(Contract.Cobrador.CUOTA_ID);
        int cliente = data.getColumnIndex(Contract.Cobrador.CLIENTE);
        int documento = data.getColumnIndex(Contract.Cobrador.CEDULA);
        int fecha = data.getColumnIndex(Contract.Cobrador.FECHA);
        int direccion = data.getColumnIndex(Contract.Cobrador.DIRECCION);
        int telefono = data.getColumnIndex(Contract.Cobrador.TELEFONO);
        int celular = data.getColumnIndex(Contract.Cobrador.CELULAR);
        int total = data.getColumnIndex(Contract.Cobrador.TOTAL);
        int prestamo = data.getColumnIndex(Contract.Cobrador.PRESTAMO);



        if (data != null) {
            for (data.moveToFirst(); !data.isAfterLast();data.moveToNext()){

                DatosCliente cli = new DatosCliente(data.getString(cuota),data.getString(cuota_id),
                        data.getString(cliente),data.getString(documento),data.getString(fecha),data.getString(direccion),
                        data.getString(telefono),data.getString(celular),data.getString(total),
                        data.getString(prestamo)
                );
                mArrayList.add(cli);
                //Log.e("Este es el ARRAY",cli.getDIRECCION());
            }



            //items = nuevoCursor;

        }
        adaptador = new AdaptadorCuotas(getContext(),this,mArrayList);
        reciclador.setAdapter(adaptador);
        if(adaptador.getItemCount()>0){
            mInfoNoData.setVisibility(View.GONE);
            reciclador.setVisibility(View.VISIBLE);
        }else{
            mInfoNoData.setVisibility(View.VISIBLE);
            reciclador.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void mostrarDetalles(Uri uri,double montoPendiente,String nombre) {
        Intent intent = new Intent(getActivity(), Pagos.class);
        if (null != uri) {
            intent.putExtra(Contract.PRESTAMOS, uri.toString());
            intent.putExtra(Contract.Cobrador.TOTAL,montoPendiente);
            intent.putExtra(Contract.Prestamo.ID, Contract.Prestamo.obtenerIdPrestamo(uri));
            intent.putExtra(Contract.Cobrador.CLIENTE,nombre);
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
    public void onClick(String idContacto,double montoPendiente,String nombre) {
        Log.e("idcontacto",idContacto);
        mostrarDetalles(Contract.PrestamoDetalle.crearUriPrestamoDetalle(idContacto),montoPendiente,nombre);
    }

    @Override
    public void showFoto(String documento) {
        final AlertDialog pDialog;


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Foto");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_foto_layout,null);
        builder.setView(view);
        ImageView foto = (ImageView) view.findViewById(R.id.DialogFoto_Foto);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(getResources().getDrawable(R.drawable.ic_image))
                .error(getResources().getDrawable(R.drawable.ic_broken_image));

        String Url = URL.FOTO+documento+".jpg";
        Glide.with(this).load(Url).apply(options).into(foto);


        pDialog = builder.create();
        pDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receptorSync);
    }







}

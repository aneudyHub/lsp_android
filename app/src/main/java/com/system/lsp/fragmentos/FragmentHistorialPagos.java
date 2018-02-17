package com.system.lsp.fragmentos;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.system.lsp.R;
import com.system.lsp.modelo.CuotaPaga;
import com.system.lsp.modelo.DatosCliente;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorHisotiralPagos;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.UTiempo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by Suarez on 13/01/2018.
 */

public class FragmentHistorialPagos extends android.support.v4.app.Fragment implements View.OnClickListener {

    public static final String TAG = "Prestamos";
    // Referencias UI
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    public List<CuotaPaga> listaCuotaPendiente;
    public OperacionesBaseDatos operacionesBaseDatos;
    private AdaptadorHisotiralPagos adaptador;
    private int REQ_DET=100;
    private DatosCliente cli;
    private TextView mTotalFacutado;
    public static String valor;
    public static String fechaBusca;

    private Button fechaConsulta;
    private EditText fechaBuscar;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    com.system.lsp.sync.HistorialPagos historialPagos;
    SwipeRefreshLayout swipeRefreshLayout;
    Calendar newCalendar;


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
        final View view = inflater.inflate(R.layout.fragment_historial_pagos, container, false);
        setHasOptionsMenu(true);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        historialPagos = new com.system.lsp.sync.HistorialPagos(getContext());
        prepararLista(view);
        setDateTimeField();

        receptorSync = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //mostrarProgreso(false);
                Log.e("broadcast","llego0000000000");
                swipeRefreshLayout.setRefreshing(false);
                String mensaje = intent.getStringExtra("extra.mensaje");

                Snackbar.make(view.findViewById(R.id.historial),
                        mensaje, Snackbar.LENGTH_LONG).show();
                if (mensaje == "Listo"){
                    Log.e("Hola soy ","AC");
                    //getData(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                }
               // getData(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            }

        };


                //getData(neewCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        newCalendar = Calendar.getInstance();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getData(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });



        return  view;
    }




    private void prepararLista(View view) {
        Log.d(TAG, "Solicitando sincronización manual");
        fechaBuscar = (EditText)view.findViewById(R.id.fechaBuscar);
        fechaBuscar.setInputType(InputType.TYPE_NULL);
        fechaBuscar.setText(UTiempo.obtenerFecha());


        mTotalFacutado=(TextView)view.findViewById(R.id.montoTotal);

        fechaBuscar.setOnClickListener(this);

       //fechaConsulta = (Button) view.findViewById(R.id.consultar);
       // fechaConsulta.setOnClickListener(this);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getContext());
        reciclador.setLayoutManager(layoutManager);




    }


    public void onClick(View view) {
        if(view == fechaBuscar) {
            toDatePickerDialog.show();


        } /*if(view == fechaConsulta){
            String fecha = fechaBuscar.getText().toString();
            operacionesBaseDatos = OperacionesBaseDatos
                    .obtenerInstancia(getContext());
            listaCuotaPendiente = operacionesBaseDatos.getCutaPagas(UPreferencias.obtenerIdUsuario(getContext()),fecha);
            adaptador = new AdaptadorHisotiralPagos(listaCuotaPendiente,getContext());
            reciclador.setAdapter(adaptador);
        }*/
    }


    private void setDateTimeField() {
       newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                getData(year,monthOfYear,dayOfMonth);

            }



        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void getData(int year, int monthOfYear, int dayOfMonth){
        newCalendar.set(year,monthOfYear,dayOfMonth);
        Log.e("E DADO CLICK","N");
        fechaBuscar.setText(dateFormatter.format(newCalendar.getTime()));
        String fecha = fechaBuscar.getText().toString();
        Log.e("VALOR-FECH",fecha);

        historialPagos.synCuotaPagaLocal(fecha);

        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());
        listaCuotaPendiente = operacionesBaseDatos.getCutaPagas(UPreferencias.obtenerIdUsuario(getContext()),fecha);
        double totalFacturado=0;
        for(CuotaPaga cu :listaCuotaPendiente){
            totalFacturado+=cu.getMonto();
        }
        mTotalFacutado.setText(String.valueOf(totalFacturado));

        adaptador = new AdaptadorHisotiralPagos(listaCuotaPendiente,getContext());
        reciclador.setAdapter(adaptador);
        swipeRefreshLayout.setRefreshing(false);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        //MenuItem search = menu.findItem(R.id.search);
       // SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //search(searchView);

    }




   /* private void search(SearchView searchView) {

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
    }*/



    @Override
    public void onDestroy() {
        super.onDestroy();
        //LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receptorSync);
    }
}
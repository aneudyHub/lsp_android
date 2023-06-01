package com.system.lsp.fragmentos;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.system.lsp.R;
import com.system.lsp.modelo.CuotaPaga;
import com.system.lsp.modelo.DatosCliente;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorHisotiralPagos;
import com.system.lsp.utilidades.Resolve;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.UTiempo;
import com.system.lsp.utilidades.ZebraPrint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by Suarez on 13/01/2018.
 */

public class FragmentHistorialPagos extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    public static final String TAG = "Prestamos";
    // Referencias UI
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    public List<CuotaPaga> listaCuotasCobradas;
    public List<CuotaPaga> listaCobrosRealizados;
    public OperacionesBaseDatos operacionesBaseDatos;
    private AdaptadorHisotiralPagos adaptador;
    private int REQ_DET=100;
    private DatosCliente cli;
    private TextView mTotalFacutado;
    public static String valor;
    public static String fechaBusca;
    public TextView mCant;
    private SharedPreferences sharedPref ;

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
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
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


        // getData(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

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
        mCant = (TextView)view.findViewById(R.id.cant);



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

            DatabaseUtils.dumpCursor(operacionesBaseDatos.obtenerSyncTime(UPreferencias.obtenerIdUsuario(getContext())));
            if (operacionesBaseDatos.isCuotasPagasExists()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                // set title
                alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>SINCRONIZACION</font>"));

                // set dialog message
                alertDialogBuilder
                        .setMessage(Html.fromHtml("Sincronizacion pediente.<br/><br/>" +
                                "<font color='#FF0000'> Porfavor SINCRONICE y vuelva a intentar</font>") )
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                Resolve.sincronizarData(getActivity());
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }else {
                toDatePickerDialog.show();
            }

            // [QUERIES]
            /*Log.d("PAGOS","PAGOS");
            Log.d("PAGOS",String.valueOf(operacionesBaseDatos.isCuotasPagasExists()));*/
            DatabaseUtils.dumpCursor(operacionesBaseDatos.pagosPendiente());


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
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());

        if (operacionesBaseDatos.isCuotasPagasExists()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            // set title
            alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>SINCRONIZACION</font>"));

            // set dialog message
            alertDialogBuilder
                    .setMessage(Html.fromHtml("Sincronizacion pediente.<br/><br/>" +
                            "<font color='#FF0000'> Porfavor SINCRONICE y vuelva a intentar</font>") )
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            Resolve.sincronizarData(getActivity());
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }else {
            historialPagos.synCuotaPagaLocal(fecha);
        }

        listaCuotasCobradas = operacionesBaseDatos.getCutaPagas(UPreferencias.obtenerIdUsuario(getContext()),fecha);
        double totalFacturado=0;
        for(CuotaPaga cu :listaCuotasCobradas){
            totalFacturado+=cu.getMonto();
        }
        mTotalFacutado.setText(String.valueOf(totalFacturado));

        adaptador = new AdaptadorHisotiralPagos((ArrayList<CuotaPaga>) listaCuotasCobradas,getContext());
        reciclador.setAdapter(adaptador);
        mCant.setText(String.valueOf(adaptador.getItemCount()));
        swipeRefreshLayout.setRefreshing(false);
    }


    private void imprimirCuadre(){
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());
        listaCobrosRealizados = operacionesBaseDatos.getImprimirCuadre(UPreferencias.obtenerIdUsuario(getContext()));

        String nombreCobrador="";
        String fechaCobro = "";
        double totalCobrado = 0;
        StringBuilder sb= new StringBuilder() ;

        for (CuotaPaga cuotaPaga :listaCobrosRealizados){
            nombreCobrador = cuotaPaga.getNombreCobrador();
            fechaCobro = cuotaPaga.getFechaConsulta();
            sb.append(cuotaPaga.getPrestamoId()+";"+cuotaPaga.getMonto()+";");
            totalCobrado +=cuotaPaga.getMonto();
        }

        Log.e("CUADRE",""+nombreCobrador+" "+sb.toString() +" Total:"+totalCobrado);

            ZebraPrint zebraprint = new ZebraPrint(getContext(),"imprimirCuadre",nombreCobrador,fechaCobro,sb.toString(),
                                                    totalCobrado);
            zebraprint.probarlo();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.imprimirCuadre) {
            /*ZebraprintOld zebraprint = new ZebraprintOld(this,null,"prueba");
            zebraprint.probarlo();*/

            imprimirCuadre();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_historial_pagos, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem imprmirCuadre = menu.findItem(R.id.imprimirCuadre);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

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
    public void onDestroy() {
        super.onDestroy();
        //LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receptorSync);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
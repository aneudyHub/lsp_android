package com.system.lsp.ui.Main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.system.lsp.R;
import com.system.lsp.fragmentos.FragmentHistorialPagos;
import com.system.lsp.fragmentos.FragmentListaCoutas;
import com.system.lsp.fragmentos.FragmentListaPrestamos;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.ui.AdaptadorCuotas;
import com.system.lsp.ui.Login.LoginActivity;
import com.system.lsp.ui.Pagos.Pagos;
import com.system.lsp.utilidades.Progress;
import com.system.lsp.utilidades.UPreferencias;
import com.system.lsp.utilidades.ZebraPrint;
import com.system.lsp.utilidades.ZebraprintOld;


public class MainActivity extends AppCompatActivity
        implements Progress, NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler;
    private SessionManager session;
    private AdaptadorCuotas mAdapter;
    private TextView nombreUsuario;
    private ProgressDialog server_prog;
    public ProgressDialog mPrinterProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mPrinterProgress = new ProgressDialog(this);
        mPrinterProgress.setTitle("Printing...");
        mPrinterProgress.setCancelable(false);
        mPrinterProgress.setIndeterminate(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView correo = (TextView) hView.findViewById(R.id.nombre_usuario);
        String nUsuario = UPreferencias.obtenerNombreUsuario(this);

        correo.setText(nUsuario);
        mHandler = new Handler();

        // session manager
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        Cursor userdb = getContentResolver().query(Contract.Cobrador.URI_CONTENIDO,null,null,null,null);

        if(userdb==null){
            logoutUser();
        }
        userdb.moveToNext();

        String claveApi = userdb.getString(userdb.getColumnIndex(Contract.Cobrador.TOKEN));
        if (claveApi==null){
            logoutUser();
            Log.e("Esta es la api",claveApi);
        }
        Log.e("Esta es la api",claveApi);
        // Reemplaza con tu clave
        UPreferencias.guardarClaveApi(this,claveApi);

        setFragment(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*ZebraprintOld zebraprint = new ZebraprintOld(this,null,"prueba");
            zebraprint.probarlo();*/

            ZebraPrint zebraprint = new ZebraPrint(this,null,"prueba",this);
            zebraprint.probarlo();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_princial:
                setFragment(0);
                break;

            case R.id.nav_prestamos:
                setFragment(1);
                break;

            case R.id.nav_h_pagos:
                setFragment(2);
                break;

            case R.id.nav_logout:
                logoutUser();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int position) {

        switch (position) {
            case 0:
            {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager;
                        FragmentTransaction fragmentTransaction;
                        // update the main content by replacing fragments
                        fragmentManager = getSupportFragmentManager();
                        FragmentListaCoutas main_fragment = new FragmentListaCoutas();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                          //      android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.container, main_fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        getSupportActionBar().setTitle("COBRAR CUOTAS");
                        Log.e("Estoy:"," en el FRAGMENT PRINCIPAL");
                    }
                };

                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }

                break;
            }


            case 1:
            {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager;
                        FragmentTransaction fragmentTransaction;
                        // update the main content by replacing fragments
                        fragmentManager = getSupportFragmentManager();
                        FragmentListaPrestamos main_fragment = new FragmentListaPrestamos();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.container, main_fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        getSupportActionBar().setTitle("PRESTAMOS");
                    }
                };

                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }

                break;
            }

            case 2:
            {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager;
                        FragmentTransaction fragmentTransaction;
                        // update the main content by replacing fragments
                        fragmentManager = getSupportFragmentManager();
                        FragmentHistorialPagos main_fragment = new FragmentHistorialPagos();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.container, main_fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        getSupportActionBar().setTitle("PRESTAMOS");
                    }
                };

                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }

                break;
            }

        }
    }


    public void logoutUser() {
        SessionManager session=  session = new SessionManager(getApplicationContext());

        session.setLogin(false);
        getContentResolver().delete(Contract.Cobrador.URI_CONTENIDO,null,null);

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void showAlert(String mensaje){
        if(server_prog!=null)
            server_prog.dismiss();


        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(mensaje);

        alertDialogBuilder.setPositiveButton("salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/

         /*Snackbar.make(findViewById(R.id.coordinador),
                            "No hay conexion disponible",
                            Snackbar.LENGTH_LONG).show();*/

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFF'>INFORMACION</font>"));

        // set dialog message
        alertDialogBuilder
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        mPrinterProgress.dismiss();
                        //finish();
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    @Override
    public void showProgressPrint(Boolean b) {
        Log.e("HOLA","EL PROBLEMA 1.0");
        if(b){
            Log.e("HOLA","EL PROBLEMA 1.1");
            mPrinterProgress.show();
        }else{
            Log.e("HOLA","EL PROBLEMA 1.2");
            mPrinterProgress.dismiss();
        }
    }

    @Override
    public void error(String msj) {
        Log.e("valor",mPrinterProgress.toString());
        if(mPrinterProgress!=null){
            mPrinterProgress.dismiss();
        }
        Log.e("error printer",msj);
        showAlert(msj);
    }

    @Override
    public void finishPrint(String msj) {
        setResult(RESULT_OK);
        showAlert(msj);
    }
}

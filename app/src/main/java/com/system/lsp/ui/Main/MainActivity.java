package com.system.lsp.ui.Main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.system.lsp.R;
import com.system.lsp.fragmentos.FragmentHistorialPagos;
import com.system.lsp.fragmentos.FragmentListaCoutas;
import com.system.lsp.fragmentos.FragmentListaPrestamos;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.SessionManager;
import com.system.lsp.ui.AdaptadorCuotas;
import com.system.lsp.ui.activities.LoginActivity;
import com.system.lsp.utilidades.UPreferencias;
import com.lsp.printer.ZebraPrint;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler;
    private SessionManager session;
    private AdaptadorCuotas mAdapter;
    private TextView nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // Reemplaza con tu clave
        UPreferencias.guardarClaveApi(this, userdb.getString(userdb.getColumnIndex(Contract.Cobrador.TOKEN)));

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

            ZebraPrint zebraprint = new ZebraPrint(this,null,"prueba");
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
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.container, main_fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        getSupportActionBar().setTitle("COBRAR CUOTAS");
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


}

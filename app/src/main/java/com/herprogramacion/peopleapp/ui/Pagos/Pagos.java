package com.herprogramacion.peopleapp.ui.Pagos;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.herprogramacion.peopleapp.R;
import com.herprogramacion.peopleapp.provider.Contract;
import com.herprogramacion.peopleapp.ui.AdaptadorCuotas;
import com.herprogramacion.peopleapp.ui.Login.LoginActivity;
import com.herprogramacion.peopleapp.utilidades.Resolve;
import com.herprogramacion.peopleapp.utilidades.UPreferencias;
import com.herprogramacion.peopleapp.utilidades.UTiempo;
import com.herprogramacion.peopleapp.utilidades.ZebraPrint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pagos extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String mPrestamoURI;
    public static String  idPrestamos;
    private RecyclerView mList;
    public static EditText mMonto;
    private TextView mPendiente,mPagar;
    private CuotasAdapter mAdapter;
    private double montoAPagar;
    private double pagarmonto;
    private double montoDigitado;
    private String nombreCliente;
    private String idCliente;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);


        mPrestamoURI = (String) getIntent().getExtras().get(Contract.PRESTAMOS);
        montoAPagar = (Double) getIntent().getExtras().get(Contract.Cobrador.TOTAL);
        idPrestamos = (String) getIntent().getExtras().get(Contract.Prestamo.ID);
        nombreCliente  = (String) getIntent().getExtras().get(Contract.Cobrador.CLIENTE);
        idCliente = (String) getIntent().getExtras().get(Contract.Cliente.ID);
        Log.e("VALOR PAGARMONTO",""+pagarmonto);
        Log.e("uri",mPrestamoURI);
        Log.e("TOTAL-PAGA",""+String.valueOf(montoAPagar));
        Log.e("VALOR ID-PRESTAMO",idPrestamos);
        Log.e("VALOR ID-CLIENTE",idCliente);
        getSupportLoaderManager().restartLoader(1, null, this);

        prepareView();
    }

    private void prepareView(){
        mList = (RecyclerView)findViewById(R.id.Lista);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mList.setLayoutManager(layoutManager);

        mMonto = (EditText)findViewById(R.id.Monto);
        mMonto.setText(String.valueOf(montoAPagar));
        mMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("monto",charSequence.toString());
                if(mAdapter!=null){
                    if(!charSequence.toString().equals("")){
                        montoDigitado = Double.parseDouble(charSequence.toString());
                        mAdapter.marcarCuotas(Double.parseDouble(charSequence.toString()));
                        montoAPagar = (Double.parseDouble(charSequence.toString()));
                    }else{
                        mAdapter.marcarCuotas(0);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPendiente =(TextView)findViewById(R.id.total_pendiente);
        mPagar =(TextView)findViewById(R.id.total_pagar);
        mPagar.setText(String.valueOf(montoAPagar));
        montoDigitado = Double.parseDouble(mMonto.getText().toString());
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cuotas a Pagar");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pagos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Procesar) {

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("ESTA SEGURO DE REALIZAR EL PAGO DE: "+ montoDigitado +" ?");

            alertDialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    pagar();
                }
            });

            alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("Estos son los datos",""+CuotasAdapter.datos);
                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(mPrestamoURI);
        return new CursorLoader(this,uri,null,Contract.PrestamoDetalle.PAGADO+"=?",new String[]{"0"},Contract.PrestamoDetalle.FECHA);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new CuotasAdapter(this);
        mAdapter.swapCursor(data);
        mList.setAdapter(mAdapter);
        setTotalPendiente(mAdapter.getTotalPendiente());
        mAdapter.marcarCuotas(montoDigitado);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setTotalPendiente(double r){
        DecimalFormat precision = new DecimalFormat("0.00");
        mPendiente.setText(precision.format(r));
    }


    public void pagar(){

        ContentValues valores = new ContentValues();

        //fecha-pago,ip-prestamo,nombre-cliente,detallpago,total-pagado,nombre-cobrador
        Log.e("FECHA",""+  UTiempo.obtenerTiempo());
        Log.e("ID-COBRADOR",""+ UPreferencias.obtenerIdUsuario(this));
        Log.e("NOMBRE-COBRADOR",""+ UPreferencias.obtenerNombreUsuario(this));
        Log.e("NOMBRE CLIENTE",nombreCliente);
        Log.e("MONTO",""+  mMonto.getText().toString());
        Log.e("ID-PRESTAMO",""+  idPrestamos);
        Log.e("FFECHA CONSULTA",""+UTiempo.obtenerFecha());
        Log.e("CADENA STRING", CuotasAdapter.datos);
        Log.e("TOTAL-MORA-P",String.valueOf(CuotasAdapter.totalMora));


        valores.put(Contract.CuotaPaga.ID,Contract.CuotaPaga.generarIdCuotasPaga());
        valores.put(Contract.CuotaPaga.FECHA,UTiempo.obtenerTiempo());
        valores.put(Contract.CuotaPaga.COBRADOR_ID,UPreferencias.obtenerIdUsuario(this));
        valores.put(Contract.CuotaPaga.NOMBRE_COBRADOR,UPreferencias.obtenerNombreUsuario(this));
        valores.put(Contract.CuotaPaga.NOMBRE_CLIENTE,nombreCliente);
        valores.put(Contract.CuotaPaga.MONTO,mMonto.getText().toString());
        valores.put(Contract.CuotaPaga.TOTALMORA,CuotasAdapter.totalMora);
        valores.put(Contract.CuotaPaga.PRESTAMO,idPrestamos);
        valores.put(Contract.CuotaPaga.FECHA_CONSULTA,UTiempo.obtenerFecha());
        valores.put(Contract.CuotaPaga.UPDATE_AT,UTiempo.obtenerTiempo());
        valores.put(Contract.CuotaPaga.CADENA_STRING,CuotasAdapter.datos);
        valores.put(Contract.CuotaPaga.INSERTADO,"1");



        new Pagos.PagarCuotas(getContentResolver(),valores).execute(Contract.CuotaPaga.URI_CONTENIDO);
    }

    class PagarCuotas extends AsyncTask<Uri,Void,Void>{
        private final ContentResolver resolver;
        private final ContentValues valores;

        public PagarCuotas(ContentResolver resolver, ContentValues valores) {
            this.resolver = resolver;
            this.valores = valores;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            Uri uri = uris[0];
            Log.e("Valor de la uri"," "+uri);
            if (null != uri) {
                resolver.insert(uri,valores);

                Log.e("ENTRE"," "+uri);
                ArrayList<ContentProviderOperation> ops  = new ArrayList<>();
                HashMap<String,String[]> y = mAdapter.getModificaCuota();

                Log.e("ENTRE 1"," "+uri);
                for(Map.Entry<String,String[]> h:y.entrySet()){
                    String key = h.getKey();
                    Log.e("VALOR-KEY",key);
                    String[] value = h.getValue();
                    Log.e("VALOR VALUE",value[0]);
                    Log.e("VALOR VALUE",value[1]);

                    if (value[0]== "2" || value[0]== "3"){
                        Log.e("ABONO MORA","ESTO ES PAR TI");
                        Uri uri1 = Contract.PrestamoDetalle.crearUriPrestamoDetalle(key);
                        ContentProviderOperation k =
                                ContentProviderOperation.newUpdate(uri1)
                                        .withValue(Contract.PrestamoDetalle.ABONO_MORA,value[1])
                                        .withValue(Contract.PrestamoDetalle.MONTO_PAGADO,value[2])

                                        .build();
                        ops.add(k);
                    }else {
                        Uri uri1 = Contract.PrestamoDetalle.crearUriPrestamoDetalle(key);
                        ContentProviderOperation k =
                                ContentProviderOperation.newUpdate(uri1)
                                        .withValue(Contract.PrestamoDetalle.PAGADO,value[0])
                                        .withValue(Contract.PrestamoDetalle.MONTO_PAGADO,value[1])

                                        .build();
                        ops.add(k);

                    }
                }
                try {
                    Log.e("ENTRE 3"," "+uri);
                    resolver.applyBatch(Contract.AUTORIDAD,ops);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }


            }else {
                Log.e("PROBLEMA CON LA URI","URI INVALIDA");


            }



            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Resolve.sincronizarData(Pagos.this);
            setResult(RESULT_OK);
            Log.e("TOTAL-E-MORA",String.valueOf(CuotasAdapter.totalMora));
            ZebraPrint zebraprint = new ZebraPrint(Pagos.this,"imprimir",UTiempo.obtenerFechaHora(),idPrestamos,nombreCliente,
                                                    CuotasAdapter.datos,montoDigitado,CuotasAdapter.totalMora,
                                                    UPreferencias.obtenerNombreUsuario(Pagos.this),
                                                    UPreferencias.obtenerTelefonoCobrador(Pagos.this));
            zebraprint.probarlo();

            finish();
        }
    }


}

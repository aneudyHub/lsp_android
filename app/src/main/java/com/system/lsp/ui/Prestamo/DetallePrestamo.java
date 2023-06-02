package com.system.lsp.ui.Prestamo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.system.lsp.R;
import com.system.lsp.fragmentos.FragmentPrestamoPagado;
import com.system.lsp.fragmentos.FragmentPrestamoPediente;
import com.system.lsp.fragmentos.FragmentTodasLasCuotas;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.Pagos.Pagos;
import com.system.lsp.utilidades.UPreferencias;

public class DetallePrestamo extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private OperacionesBaseDatos datos;
    private Cursor cursor;
    private Context ctx;

    private int REQ_DET=100;

    private String idPrestamos;
    private Double monto;
    private Double totalCuota;

    private  TextView capital;
    private  TextView interes;
    private  TextView mora;
    private  TextView balance;
    private  TextView fecha;
    private  TextView cuotas;
    private  TextView pagas;
    private  TextView pendiente;
    private  TextView nombre;
    private  TextView documento;
    private  TextView iPrestamo;
    private  Button adelantarPago;
    public OperacionesBaseDatos operacionesBaseDatos;
    private Double montoTotal;




    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_prestamo);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        balance = (TextView)findViewById(R.id.balance);


        idPrestamos = (String) getIntent().getExtras().get(Contract.Prestamo.ID);

        UPreferencias.guardarIdPrestamos(this,idPrestamos);

        Log.e("Este es el valor",idPrestamos);
        getSupportActionBar().setTitle("Detalle Prestamo");

        capital = (TextView) findViewById(R.id.capital);
        interes = (TextView) findViewById(R.id.interes);
        mora = (TextView) findViewById(R.id.mora);
        balance = (TextView) findViewById(R.id.balance);
        fecha = (TextView) findViewById(R.id.fecha);
        cuotas = (TextView) findViewById(R.id.cuotas);
        pagas = (TextView) findViewById(R.id.pagada);
        pendiente = (TextView) findViewById(R.id.pendiente);
        nombre = (TextView) findViewById(R.id.nombre_cliente);
        documento = (TextView) findViewById(R.id.documento);
        iPrestamo = (TextView) findViewById(R.id.idprestamo);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());


        adelantarPago = (Button)findViewById(R.id.adelantarPago);
        adelantarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetallePrestamo.this, Pagos.class);
                Uri uri;

                uri = Contract.PrestamoDetalle.crearUriPrestamoDetalle(idPrestamos);
                Log.e("URI-PRE",uri.toString());
                Log.e("ID-PRESTAMO",Contract.Prestamo.obtenerIdPrestamo(uri));
                Log.e("URI-PRE",nombre.getText().toString());
                if (null != uri) {
                    intent.putExtra(Contract.PRESTAMOS, uri.toString());
                    intent.putExtra(Contract.Cobrador.TOTAL,monto);
                    intent.putExtra("TotalCuota",totalCuota);
                    intent.putExtra(Contract.Prestamo.ID, Contract.Prestamo.obtenerIdPrestamo(uri));
                    intent.putExtra(Contract.Cobrador.CLIENTE,nombre.getText());




                }
                startActivityForResult(intent,REQ_DET);
            }
        });

        new TareaPruebaDatos().execute();

    }

    void mostrarDetalles(Uri uri, double montoPendiente, String nombre) {
        Intent intent = new Intent(this, Pagos.class);
        if (null != uri) {
            intent.putExtra(Contract.PRESTAMOS, uri.toString());
            intent.putExtra(Contract.Cobrador.TOTAL,montoPendiente);
            intent.putExtra(Contract.Prestamo.ID, Contract.Prestamo.obtenerIdPrestamo(uri));
            intent.putExtra(Contract.Cobrador.CLIENTE,nombre);
        }
        startActivityForResult(intent,REQ_DET);
    }


    private void consultar(String id)
    {
        //
        // Consultamos el centro por el identificador
        //
        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(this);
        int totalPendiente = operacionesBaseDatos.getCuotaPendiete(UPreferencias.obtenerIdPrestamos(this),"0").size();
        int totalPagada = operacionesBaseDatos.getCuotaPendiete(UPreferencias.obtenerIdPrestamos(this),"1").size();


        cursor = datos.ObtenerDatosPrestamoPorId(id);

        Log.e("TAOTAL-PENDIENTE",String.valueOf(totalPendiente));

        capital.setText("RD$ "+cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CAPITAL)));
        interes.setText("RD$ "+cursor.getString(cursor.getColumnIndex(Contract.PrestamoDetalle.INTERES)));
        mora.setText("RD$ "+cursor.getString(cursor.getColumnIndex(Contract.PrestamoDetalle.MORA)));
        balance.setText("RD$ "+cursor.getString(cursor.getColumnIndex("ValorCapital")));
        //double b =Double.parseDouble(cursor.getString(cursor.getColumnIndex(Contract.PrestamoDetalle.CAPITAL)));
        monto = datos.obtenerTotalAPagar(idPrestamos);
        totalCuota = datos.obtenerTotalCuota(idPrestamos);

        fecha.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.FECHA_INICIO)));
        cuotas.setText(cursor.getString(cursor.getColumnIndex(Contract.Prestamo.CUOTAS)));
        pagas.setText(String.valueOf(totalPagada));
        pendiente.setText(String.valueOf(totalPendiente));
        nombre.setText(cursor.getString(cursor.getColumnIndex(Contract.Cliente.NOMBRE)));
        documento.setText(cursor.getString(cursor.getColumnIndex(Contract.Cliente.DOCUMENTO)));
        iPrestamo.setText("# "+idPrestamos);


    }


    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // [QUERIES]
           Log.d("Clientes","Clientes");
            //DatabaseUtils.dumpCursor(datos.ObtenerDatosPrestamoPorId(idPrestamos));
            consultar(idPrestamos);

            return null;
        }
    }






    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detalle_prestamo, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);


            switch (position){
                case 0:
                    FragmentTodasLasCuotas todosLosPrestamo = new FragmentTodasLasCuotas();
                    return todosLosPrestamo;
                case 1:
                    FragmentPrestamoPediente fragmentPrestamoPediente = new FragmentPrestamoPediente();
                    return fragmentPrestamoPediente;
                case 2:
                    FragmentPrestamoPagado fragmentPrestamoPagado = new FragmentPrestamoPagado();
                    return fragmentPrestamoPagado;

            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }
}

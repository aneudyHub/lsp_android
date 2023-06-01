package com.system.lsp.fragmentos;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.system.lsp.R;
import com.system.lsp.modelo.PrestamoDetalle;
import com.system.lsp.provider.Contract;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorTodasLasCuotas;
import com.system.lsp.ui.Prestamo.DetallePrestamo;
import com.system.lsp.utilidades.UPreferencias;

import java.util.ArrayList;


public class FragmentTodasLasCuotas extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorTodasLasCuotas adaptador;
    private DetallePrestamo detalle;

    private OperacionesBaseDatos datos;
    private Cursor cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("DONDE ESTOY","PORFA");
        // Inflate the layout for this fragment
        final View view =   inflater.inflate(R.layout.fragment_todos_los_prestamo, container, false);
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);
        prepararLista(view);




        return  view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(Contract.PrestamoDetalle.URI_CONTENIDO,""+UPreferencias.obtenerIdPrestamos(getContext()));
        Log.e("Estea es la uri","Detalle "+uri);
        return new CursorLoader(
                getContext(),
                uri,null,null,null,null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.e("Este es el id","prestamos" + UPreferencias.obtenerIdPrestamos(getContext()));

        Log.e("cursor listaaaaa",data.toString());
        ArrayList<PrestamoDetalle> mArrayList = new ArrayList<PrestamoDetalle>();
        Log.e("DONDE ESTOY","PORFA");
        int cuota = data.getColumnIndex(Contract.PrestamoDetalle.CUOTA);
        int capital = data.getColumnIndex(Contract.PrestamoDetalle.CAPITAL);
        int interes = data.getColumnIndex(Contract.PrestamoDetalle.INTERES);
        int mora = data.getColumnIndex(Contract.PrestamoDetalle.MORA);
        int fecha = data.getColumnIndex(Contract.PrestamoDetalle.FECHA);
        int pagado = data.getColumnIndex(Contract.PrestamoDetalle.PAGADO);
        Log.e("DONDE ESTOY","PORFA");
        if (data != null) {
            for (data.moveToFirst(); !data.isAfterLast();data.moveToNext()){

                PrestamoDetalle cli = new PrestamoDetalle(data.getInt(cuota),data.getDouble(capital),
                        data.getDouble(interes),data.getDouble(mora),data.getString(fecha),
                        data.getInt(pagado)
                );
                mArrayList.add(cli);
                //Log.e("Este es el ARRAY",String.valueOf(cli.getCapital()));
            }

            Log.e("DONDE ESTOY","PORFA");

            //items = nuevoCursor;

        }
        adaptador = new AdaptadorTodasLasCuotas(mArrayList);
        reciclador.setAdapter(adaptador);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




    private void prepararLista(View view) {
        Log.e("DONDE ESTOY","PORFA");
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

    }

}

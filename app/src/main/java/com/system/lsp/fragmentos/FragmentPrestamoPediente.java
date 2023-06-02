package com.system.lsp.fragmentos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.system.lsp.R;
import com.system.lsp.modelo.CuotaPendiente;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorCuotasPendientes;
import com.system.lsp.utilidades.UPreferencias;

import java.util.List;


public class FragmentPrestamoPediente extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    public AdaptadorCuotasPendientes adaptadorCuotasPendientes;
    public List<CuotaPendiente> listaCuotaPendiente;
    public OperacionesBaseDatos operacionesBaseDatos;
    public static int totalPendiente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("DONDE ESTOY","PORFA");
        // Inflate the layout for this fragment
        final View view =   inflater.inflate(R.layout.fragment_prestamo_pendiente, container, false);
        //getActivity().getSupportLoaderManager().restartLoader(1, null, this);

        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());
       listaCuotaPendiente = operacionesBaseDatos.getCuotaPendiete(UPreferencias.obtenerIdPrestamos(getContext()),"0");

       totalPendiente = listaCuotaPendiente.size();

        prepararLista(view);




        return  view;
    }



    private void prepararLista(View view) {
        Log.e("DONDE ESTOY","PORFA");
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador_cuotas_pendientes);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        adaptadorCuotasPendientes = new AdaptadorCuotasPendientes(listaCuotaPendiente,getContext());

        reciclador.setAdapter(adaptadorCuotasPendientes);
    }


}

package com.system.lsp.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.lsp.R;
import com.system.lsp.modelo.CuotaPendiente;
import com.system.lsp.provider.OperacionesBaseDatos;
import com.system.lsp.ui.AdaptadorCuotasPagadas;
import com.system.lsp.ui.AdaptadorCuotasPendientes;
import com.system.lsp.utilidades.UPreferencias;

import java.util.List;


public class FragmentPrestamoPagado extends Fragment {


    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    public AdaptadorCuotasPagadas adaptadorCuotasPagadas;
    public List<CuotaPendiente> listaCuotaPendiente;
    public OperacionesBaseDatos operacionesBaseDatos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("DONDE ESTOY","PORFA");
        // Inflate the layout for this fragment
        final View view =   inflater.inflate(R.layout.fragment_prestamo_pagado, container, false);
        //getActivity().getSupportLoaderManager().restartLoader(1, null, this);

        operacionesBaseDatos = OperacionesBaseDatos
                .obtenerInstancia(getContext());
        listaCuotaPendiente = operacionesBaseDatos.getCuotaPendiete(UPreferencias.obtenerIdPrestamos(getContext()),"1");

        prepararLista(view);




        return  view;
    }



    private void prepararLista(View view) {
        Log.e("DONDE ESTOY","PORFA");
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador_cuotas_pagadas);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        adaptadorCuotasPagadas = new AdaptadorCuotasPagadas(listaCuotaPendiente,getContext());

        reciclador.setAdapter(adaptadorCuotasPagadas);
    }


}

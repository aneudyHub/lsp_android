package com.herprogramacion.peopleapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.herprogramacion.peopleapp.R;
import com.herprogramacion.peopleapp.modelo.CuotaPaga;
import com.herprogramacion.peopleapp.modelo.PrestamoDetalle;
import com.herprogramacion.peopleapp.ui.Pagos.CuotasAdapter;
import com.herprogramacion.peopleapp.ui.Pagos.Pagos;
import com.herprogramacion.peopleapp.utilidades.UPreferencias;
import com.herprogramacion.peopleapp.utilidades.UTiempo;
import com.herprogramacion.peopleapp.utilidades.ZebraPrint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suarez on 20/01/2018.
 */

public class AdaptadorHisotiralPagos extends RecyclerView.Adapter<AdaptadorHisotiralPagos.ViewHolder>  {

    private List<CuotaPaga> mArrayList;
    private Context context;

    public AdaptadorHisotiralPagos(List<CuotaPaga> mArrayList,Context context) {
        this.mArrayList=mArrayList;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombreCliente;
        public TextView fecha;
        public TextView monto;
        public View statusIndicator;
        public CardView mLayout;
        public Button mReimprimir;

        public ViewHolder(View v) {
            super(v);
            nombreCliente = (TextView) v.findViewById(R.id.nombre_cliente);
            fecha = (TextView) v.findViewById(R.id.fechaCobro);
            monto = (TextView) v.findViewById(R.id.montoCobrado);
            statusIndicator = v.findViewById(R.id.appointment_status);
            mLayout= (CardView) v.findViewById(R.id.Layout);
            mReimprimir =(Button)v.findViewById(R.id.reimprimir);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e("Este es el valor","Estoy aca");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_pagos, parent, false);
        return new AdaptadorHisotiralPagos.ViewHolder(v);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CuotaPaga c = mArrayList.get(position);

        View statusIndicator = holder.statusIndicator;
        final String fecha =  String.valueOf(c.getFecha());
        final String idPrestamos = String.valueOf(c.getPrestamoId());
        final String nombreCliente = String.valueOf(c.getNombreCliente());
        final String datos = String.valueOf(c.getCadenaString());
        final double monto = c.getMonto();
        final String nombreCobrador = String.valueOf(c.getNombreCobrador());



        holder.mReimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Soy el nombre",nombreCliente);
                Log.e("STRING:",datos);
                ZebraPrint zebraprint = new ZebraPrint(view.getContext(),"imprimir", fecha,
                        idPrestamos,nombreCliente, datos,monto, nombreCobrador);
                zebraprint.probarlo();
            }
        });


        /*Log.e("Valor Cuota",pago);
        if(pago.equals("1")){
            statusIndicator.setBackgroundResource(R.color.activeStatus);
        }else {
            statusIndicator.setBackgroundResource(R.color.colorPrimaryDark);
        }*/


        //indicator_appointment_status
        holder.nombreCliente.setText(nombreCliente);
        holder.fecha.setText(fecha);
        holder.monto.setText("RD$ "+monto);
        



    }

    @Override
    public int getItemCount() {
       return (mArrayList==null)?0:mArrayList.size();
    }






}

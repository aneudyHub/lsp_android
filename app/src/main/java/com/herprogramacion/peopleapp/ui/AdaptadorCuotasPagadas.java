package com.herprogramacion.peopleapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herprogramacion.peopleapp.R;
import com.herprogramacion.peopleapp.modelo.CuotaPendiente;

import java.util.List;

/**
 * Created by Suarez on 21/01/2018.
 */

public class AdaptadorCuotasPagadas extends RecyclerView.Adapter<AdaptadorCuotasPagadas.ViewHolder> {

    private final Context context;
    private final List<CuotaPendiente> items;

    public AdaptadorCuotasPagadas(List<CuotaPendiente> items, Context context) {
        this.items = items;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView fecha;
        public TextView cuota;
        public TextView capital;
        public TextView interes;
        public TextView mora;
        public View statusIndicator;
        public CardView mLayout;

        public ViewHolder(View v) {
            super(v);
            fecha = (TextView) v.findViewById(R.id.fecha);
            cuota = (TextView) v.findViewById(R.id.num_cuota);
            capital = (TextView) v.findViewById(R.id.capital);
            interes = (TextView) v.findViewById(R.id.interes);
            mora = (TextView) v.findViewById(R.id.mora);
            statusIndicator = itemView.findViewById(R.id.appointment_status);
            mLayout= (CardView) itemView.findViewById(R.id.Layout);
        }
    }


    @Override
    public AdaptadorCuotasPagadas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("Este es el valor","Estoy aca");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_cuotas_pagadas, parent, false);
        return new AdaptadorCuotasPagadas.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorCuotasPagadas.ViewHolder holder, int position) {

        final CuotaPendiente c = items.get(position);

        View statusIndicator = holder.statusIndicator;
        String pago =  String.valueOf(c.getPagado());
        String n_cuota = String.valueOf(c.getCuota());


        Log.e("Valor Cuota",pago);
        if(pago.equals("1")){
            statusIndicator.setBackgroundResource(R.color.activeStatus);
        }else {
            statusIndicator.setBackgroundResource(R.color.colorPrimaryDark);
        }


        //indicator_appointment_status
        Log.e("VALOR CUOTA",n_cuota);
        holder.cuota.setText(n_cuota);
        holder.fecha.setText(c.getFecha());
        holder.capital.setText("RD$ "+String.valueOf(c.getCapital()));
        holder.interes.setText("RD$ "+String.valueOf(c.getInteres()));
        holder.mora.setText("RD$ "+String.valueOf(c.getMora()));
    }

    @Override
    public int getItemCount() {
       return (items==null)?0:items.size();
    }
}
